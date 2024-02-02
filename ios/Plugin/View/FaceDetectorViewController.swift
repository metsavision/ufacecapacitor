//
//  FaceDetectorViewController.swift
//  Plugin
//
//  Created by 송재상 on 1/18/24.
//  Copyright © 2024 Max Lynch. All rights reserved.
//

import UIKit
import UFaceDetectorMango

protocol FaceDetectorViewControllerDelegate {
    func faceDetected(_ faceBase64: String)
    func faceDetectorClosed()
}


class FaceDetectorViewController: UIViewController {
    
    enum DisplayType: String {
        case blink
        case faceDetect
        case squareDetect
    }
    
    @IBOutlet weak var viewPreview: UIView!
    @IBOutlet weak var labelFaceGuide: UILabel!
    
    var faceDetector: UFaceDetector!
    var modelDirectory: String = ""
    
    var delegate: FaceDetectorViewControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.faceDetector = UFaceDetector()
        self.faceDetector.delegate = self
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.faceDetector.setAntiSpoofingUse(
            useSpoof1: true,
            useSpoof2: true,
            useSpoof3: true,
            useSpoof4: true,
            useEyeBlink: true,
            useMaskDetect: true,
            threshold1: 0.6,
            useLocalFeature: true
        )
        // 얼굴 검출 초기화 라이센스 및 모델 파일 경로 입력 필요.
        self.faceDetector.initDetector(
            licenseKey: "4F5A465276310081161DB4851F3B16C45F2F3AE46EA485506BA8783B7840FF52A8F872D7EE4B6A8F",
            modelDirectory: self.modelDirectory
        )
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if self.faceDetector != nil {
            self.faceDetector.stopDetector()
        }
    }
    
    @IBAction func close(_ sender: UIButton) {
        self.delegate?.faceDetectorClosed()
    }
    
    static func instantiate(bundle: Bundle? = nil) -> FaceDetectorViewController {
        let sb = UIStoryboard(name: "Auth", bundle: bundle)
        let vc = sb.instantiateViewController(withIdentifier: "FaceDetectorViewController") as! FaceDetectorViewController
        let modelStr = bundle?.bundlePath ?? ""
        vc.modalPresentationStyle = .fullScreen
        vc.modelDirectory = modelStr
        return vc
    }
    
}

extension FaceDetectorViewController: UFaceDetectorDelegate {
    func ufaceDetector(detector: UFaceDetectorMango.UFaceDetector, error: UFaceDetectorMango.UFaceError) {
        switch error.errorCode {
        case "72001" :
            self.labelFaceGuide.text = "Fill in the face with a square\nGuess the location"
            self.faceDetector.reStartDetector()
            break
        default :
            self.openAlertView(msg: String(format: "%@(code:%@)", error.errorDescription, error.errorCode)) {
                self.delegate?.faceDetectorClosed()
            }
            break
        }
    }
    
    func ufaceDetector(detector: UFaceDetectorMango.UFaceDetector, result: UFaceDetectorMango.UFaceResult) {
        if result.isFake {
            DispatchQueue.main.async {
                self.openAlertView(msg: "Fake attack detected.") {
                    self.delegate?.faceDetectorClosed()
                }
            }
            return
        }
        
        if result.isMask {
            DispatchQueue.main.async {
                self.openAlertView(msg: "Mask detected.") {
                    self.faceDetector.reStartDetector()
                }
            }
            return
        }
        guard let face = result.fullImage.jpegData(compressionQuality: 0.9) else {
            self.faceDetector.reStartDetector()
            return
        }
        
        let faceBase64 = face.base64EncodedString()
        self.delegate?.faceDetected(faceBase64)
    }
    
    func ufaceDetectorSetCameraSessionComplete() {
        self.faceDetector.previewLayer.frame = self.viewPreview.bounds
        self.viewPreview.layer.addSublayer(self.faceDetector.previewLayer)
        self.faceDetector.startDetect()
    }
    
    func ufaceDetectorPitchYawRoll(pitch: Float, yaw: Float, roll: Float) {}
    
    func ufaceIsDetectFace(isDetectFace: Bool) {}
    
    func ufaceDetectSmallFace() {
        print("Detector Small Face")
    }
    
    func ufaceDetectLargeFace() {
        print("Detector Large Face")

    }
}


