using FinalProject.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace FinalProject.Controllers
{
    public class DoctorController : Controller
    {
        
        private YoyagDBEntities db = new YoyagDBEntities();

        public ActionResult Done()
        {
            return View();
        }
        // GET: Doctor
        public ActionResult Index()
        {
            List<Summary> summaries = db.Summaries.GroupBy(p => p.userID)
                  .Select(g => g.OrderByDescending(p => p.timeStamp)
                                .FirstOrDefault()
                   ).ToList();
            // return View(db.Summaries.ToList());
               return View(summaries);
        }

        // GET: Doctor/patientDetails/5
        public ActionResult patientDetails(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Summary patient = db.Summaries.Find(id);
            if (patient == null)
            {
                return HttpNotFound();
            }
            List<Summary> summaries= (db.Summaries.Where(g => g.userID==patient.userID)
                                .OrderByDescending(p => p.timeStamp)
                   ).ToList();
            ViewBag.id = patient.userID;
            return View(summaries);
        }

        // GET: Doctor/summaryDetails/5
        public ActionResult summaryDetails(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Summary patient = db.Summaries.Find(id);
            if (patient == null)
            {
                return HttpNotFound();
            }
            return View(patient);
        }

            // GET: Doctor/Details/5
            public ActionResult DetailsIOS(int? id)
        {
            return View();
        }

        // GET: Patients/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Summary patient = db.Summaries.Find(id);
            if (patient == null)
            {
                return HttpNotFound();
            }
            return View(patient);
        }

        // POST: Patients/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            Summary patient = db.Summaries.Find(id);
            db.Summaries.Remove(patient);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        // GET: Doctor/Summary
        public ActionResult getSummary(Summary summary)
        {
          
                db.Summaries.Add(summary);
                db.SaveChanges();


            return RedirectToAction("Index");
        }


        public HttpStatusCodeResult updateDiagnosis(string token, string userID, string sessionID, string formData)
        {
            webproxy web = new webproxy();
            return web.update(token, userID, sessionID, formData);
          
        }

        public string getStatistics(string token, string userID, string sessionID)
        {
            webproxy web = new webproxy();
            string tmp = web.getStats(token, userID, sessionID);
            return tmp;

        }




    }
}