//
//  login-screenViewController.swift
//  Yoyag
//
//  Created by delver on 7.4.2018.
//  Copyright © 2018 delver. All rights reserved.
//

import UIKit
import Alamofire

class login_screenViewController: UIViewController,FormScreenViewControllerDelegate{

    @IBOutlet weak var UserIDTextField: UITextField!
    
    @IBOutlet weak var PasswordTextField: UITextField!
    
    @IBOutlet weak var ErrorLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let text = UserIDTextField.text!
        if segue.identifier == "moveToFormScreen" {
            let destViewController = segue.destination as! form_screen
            destViewController.userID = text
        }
        
    }
    @IBAction func LoginButton(_ sender: UIButton) {
        let ID = UserIDTextField.text
        let Pass = PasswordTextField.text
        let url = "http://db.cs.colman.ac.il/yoyag/account/IOSLogin?userID="+ID!+"&password="+Pass!
        
        Alamofire.request(url, method: .get, parameters: nil, encoding: JSONEncoding.default)
            .responseJSON { response in
                print(response)
                //to get status code
                if let status = response.response?.statusCode {
                    switch(status){
                    case 200:
                        print("example success")
                    default:
                        print("error with response status: \(status)")
                    }
                }
                //to get JSON return value
                var login = "non"
                if let result = response.result.value {
                    let JSON = result as! String
                    print(JSON)
                    login = JSON
                    
                }
                
                if (login == "True"){
                    print("user signed in")
                    self.performSegue(withIdentifier: "moveToFormScreen", sender: Any?.self)
                }
                else
                {
                    print("error signing in")
                    self.ErrorLabel!.isHidden = false
                }
                
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
