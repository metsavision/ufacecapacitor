//
//  NetworkError.swift
//  Capacitor
//
//  Created by 송재상 on 1/22/24.
//

import Foundation

enum NetworkError: Error {
    case invaliPath(url: String)
    case parseError(Error)
    case requestError(Error)
    case httpURLResponseFail(Int)
}
