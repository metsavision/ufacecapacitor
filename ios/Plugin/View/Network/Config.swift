//
//  Config.swift
//  Capacitor
//
//  Created by 송재상 on 1/22/24.
//

import Foundation

struct Config {
    
    struct API {
        static let apiURL: String = "http://ekyc.metsafr.com:3333/api/invoke"
    }
    
    struct Service {
        static let face = "face"
    }
    struct APIName {
        static let verifyID = "verifyid"
    }
    
    struct Bearer {
        static let token: String = "eyJhbGciOiJIUzI1NiJ9.eyJzZXJ2aWNlIjoib25lNHUiLCJzdWIiOiJvbmU0dSIsImp0aSI6IjE0YTdiZTExLTY1N2UtNDgwZi05M2I4LTAzMGM0ODA1ZWY4MCIsImlhdCI6MTcwMjUzMjAzMiwiZXhwIjoxNzM0MDY4MDMyfQ.4eFOmxM-WQQeucB7HWvTE2ksKindvJ2xEfgngFiQ0Ok"
    }
}
