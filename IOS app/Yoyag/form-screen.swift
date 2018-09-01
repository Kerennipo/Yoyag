//
//  form-screen.swift
//  Yoyag
//
//  Created by delver on 16.4.2018.
//  Copyright © 2018 delver. All rights reserved.
//

import UIKit
import Alamofire

protocol FormScreenViewControllerDelegate {
    
}

class form_screen: UIViewController {

    @IBOutlet weak var textArea: UITextView!
    var userID:String = ""
    var delegate:FormScreenViewControllerDelegate?
  

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
 
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

  
    @IBAction func SubmitFormButton(_ sender: UIButton) {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "M/d/yyyy, hh:mm:ss a"
        formatter.amSymbol = "AM"
        formatter.pmSymbol = "PM"
        
        let dateString = formatter.string(from: Date())
        print(dateString)
        let text: String = textArea.text
        print(text)
        
        let parameters: [String: Any] = [
           "token": "ABRAKADABR@1237" as AnyObject,
           "userID": userID,
           "timestamp": dateString,
           "data": ["text": text]
         ]
        let url = "http://193.106.55.122/post"
        
        Alamofire.request(url, method: .post, parameters: parameters, encoding: JSONEncoding.default)
            .responseJSON { response in
                print(response)
                //to get status code
                if let status = response.response?.statusCode {
                    switch(status){
                    case 204:
                        print("example success")
                    default:
                        print("error with response status: \(status)")
                    }
                }
                self.performSegue(withIdentifier: "moveToFinishSubmitScreen", sender: Any?.self)
    }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
