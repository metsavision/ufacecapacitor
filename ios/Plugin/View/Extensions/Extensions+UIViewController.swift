//
//  Extensions+UIViewController.swift
//  Ufacesdk
//
//  Created by 송재상 on 1/22/24.
//

import UIKit.UIViewController

extension UIViewController {
    func openAlertView(title:String? = nil , msg: String, okHandle: (() -> ())? = nil) {
        let alert = UIAlertController(title: title, message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (ac) in
            if okHandle != nil {
                okHandle!()
            }
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    func embed(_ viewController:UIViewController, inView view:UIView){
        viewController.willMove(toParent: self)
        viewController.view.frame = view.bounds
        view.addSubview(viewController.view)
        self.addChild(viewController)
        viewController.didMove(toParent: self)
    }
}
