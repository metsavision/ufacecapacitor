//
//  Extensions+UIView.swift
//  Ufacesdk
//
//  Created by 송재상 on 1/22/24.
//

import UIKit.UIView

extension UIView {
    func mask(rect:CGRect, corner: CGFloat ,isRectMasking: Bool) {
        let path = CGMutablePath()
        if isRectMasking {
            let allRect = CGRect(x: 0.0, y: 0.0, width: frame.size.width, height: frame.size.height)
            path.addRect(allRect)
        }
        path.addRoundedRect(in: rect, cornerWidth: corner, cornerHeight: corner)
        let shapeLayer = CAShapeLayer()
        shapeLayer.path = path
        shapeLayer.lineWidth = 5
        shapeLayer.strokeColor = UIColor.white.cgColor
        if isRectMasking {
            shapeLayer.fillRule = .evenOdd
        }
        
        layer.mask = shapeLayer
    }
}
