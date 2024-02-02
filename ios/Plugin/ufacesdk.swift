import Foundation

@objc public class ufacesdk: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
