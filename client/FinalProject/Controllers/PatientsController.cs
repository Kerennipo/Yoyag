using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using FinalProject.Models;

namespace FinalProject.Controllers
{
    public class PatientsController : Controller
    {


        //Message indicating that form was successfully submitted
        public ActionResult Done()
        {
            return View();
        }
   

        public ActionResult Create(User user)
        {   
            return View(user);
        }

        public ActionResult StringCreate(User user)
        {
            return View(user);
        }

 
        public HttpStatusCodeResult SendToServer(string token,string userID,string timestamp,string data)
        {
            string user = userID.Trim();
            webproxy web = new webproxy();
            return web.patientSubmit(token, user, timestamp, data);

        }

 

    }
}
