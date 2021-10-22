import Foundation

@objc public class Custom: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
