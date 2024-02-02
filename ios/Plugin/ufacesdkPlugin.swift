import Capacitor
import UIKit

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ufacesdkPlugin)
public class ufacesdkPlugin: CAPPlugin {
    private let implementation = ufacesdk()


    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
    
    @objc func verify(_ call: CAPPluginCall) {
        if let baseurl = call.getString("baseurl"),
           let token = call.getString("token") {
            print("token \(token)")
            print("baseurl \(baseurl)")
            DispatchQueue.main.sync {
                let pluginResourcePath = Bundle(for: ufacesdkPlugin.self)
                let vc = SquareDetectorViewController.instantiate(
                    bundle: pluginResourcePath,
                    baseurl: baseurl,
                    token: token
                ) { score in
                    self.notifyListeners("onVerified", data: [
                        "value" : score
                    ])
                    DispatchQueue.main.async {
                        self.bridge?.dismissVC(animated: true, completion: nil)
                    }
                } closedHandler: {
                    call.reject("Closed when the user touches the close button or an error occurs.")
                    DispatchQueue.main.async {
                        self.bridge?.dismissVC(animated: true, completion: nil)
                    }
                }
                
                self.bridge?.viewController?.present(vc, animated: true)
            }
        } else {
            call.reject("The url or token value is empty.")
        }
    }
}
