//
//  SquareDetectorViewController.swift
//  Ufacesdk
//
//  Created by 송재상 on 1/22/24.
//

import UIKit
import MKSquareDetector

class SquareDetectorViewController: UIViewController {
    
    @IBOutlet weak var viewPreview: UIView!
    @IBOutlet weak var viewScanBackground: UIView!
    @IBOutlet weak var viewScanBorder: UIView!
        
    private var squareDetector: SquareDetector!
    private var bundle: Bundle? = nil
    private var baseurl: String? = nil
    private var token: String? = nil
    private var completedHandler: ((String) -> ())? = nil
    private var closedHandler: (() -> ())? = nil
    
    private var idbase64: String? = nil

    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.squareDetector = SquareDetector()
        self.squareDetector.delegate = self

    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.squareDetector.initDetector()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if self.squareDetector != nil {
            self.squareDetector.stopDetector()
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        let cornerRadius: CGFloat = 16.0
        self.viewScanBorder.layer.cornerRadius = cornerRadius
        self.viewScanBorder.layer.borderColor = UIColor.white.cgColor
        self.viewScanBorder.layer.borderWidth = 2
        
        self.viewScanBackground.mask(rect: self.viewScanBorder.frame, corner: cornerRadius, isRectMasking: true)
        self.viewScanBackground.layer.masksToBounds = true
        
        if self.squareDetector.previewLayer != nil {
            self.squareDetector.previewLayer.frame = self.viewPreview.bounds
        }
        
    }
    
    @IBAction func close(_ sender: UIButton) {
        self.closedHandler?()
    }
    
    func callAPI(idCard: String, face: String) {
        let param = RequestModel(
            service: Config.Service.face,
            apiName: Config.APIName.verifyID,
            param: Param(
                id: idCard,
                face: face
            )
        )
        
        let encoder = JSONEncoder()
        var jsonData = Data()
        do {
            jsonData = try encoder.encode(param)
            let jsonString = String(data: jsonData, encoding: .utf8) ?? ""
            print(jsonString)
        } catch let error {
            let error = error.localizedDescription
            DispatchQueue.main.async {
                self.openAlertView(msg: error) {
                    self.closedHandler?()
                }
            }
        }
        
        Network.requestData(url: self.baseurl ?? "",token: self.token ?? "", jsonData: jsonData, type: ResponseModel.self) { result in
            switch result {
            case .success(let success):
                if success?.code == "00" {
                    let score = String(success?.data ?? 00)
                    self.completedHandler?(score)
                    return
                }
                self.completedHandler?("code : \(success?.code ?? "unknown"), message : \(success?.message ?? "unknown")")
                break
            case .failure(let failure):
                print("failure: \(failure.localizedDescription)")
                self.closedHandler?()
                break
            }
        }
    }
        
    static func instantiate(bundle: Bundle? = nil, baseurl: String? = nil, token: String? = nil, completedHandler: ((String) -> ())? = nil, closedHandler: (() -> ())? = nil ) -> SquareDetectorViewController {
        let sb = UIStoryboard(name: "Auth", bundle: bundle)
        let vc = sb.instantiateViewController(withIdentifier: "SquareDetectorViewController") as! SquareDetectorViewController
        vc.modalPresentationStyle = .fullScreen
        vc.bundle = bundle
        vc.baseurl = baseurl
        vc.token = token
        vc.completedHandler = completedHandler
        vc.closedHandler = closedHandler
        return vc
    }
    
}

// MARK: SquareDetectorDelegate
extension SquareDetectorViewController: SquareDetectorDelegate {
    func squareDetectorSetCameraSuccess() {
        self.squareDetector.previewLayer.frame = self.viewPreview.bounds
        self.viewPreview.layer.addSublayer(self.squareDetector.previewLayer)
        self.squareDetector.startDetector()
        
    }
    
    func squareDetector(detector: MKSquareDetector.SquareDetector, error: MKSquareDetector.MKError) {
        let errorStr = String(format: "%@(code:%@)", error.errorDescription, error.errorCode)
        self.openAlertView(msg: errorStr) {
            self.closedHandler?()
        }
    }
    
    func squareDetector(detector: MKSquareDetector.SquareDetector, result: MKSquareDetector.MKSquareResult) {
        guard let idImageData = result.idImage.jpegData(compressionQuality: 0.9) else {
            self.squareDetector.restartDetect()
            return
        }
        
        if self.squareDetector != nil {
            self.squareDetector.stopDetector()
        }
        
        self.idbase64 = idImageData.base64EncodedString()
        DispatchQueue.main.async {
            let vc = FaceDetectorViewController.instantiate(bundle: self.bundle)
            vc.delegate = self
            self.embed(vc, inView: self.view)
        }
    }
}

// MARK: FaceDetectorViewControllerDelegate
extension SquareDetectorViewController: FaceDetectorViewControllerDelegate {
    func faceDetected(_ faceBase64: String) {
        self.callAPI(idCard: self.idbase64 ?? "", face: faceBase64)
    }
    
    func faceDetectorClosed() {
        self.closedHandler?()
    }
}
