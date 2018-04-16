using FinalProject.Models;
using System;
using System.Collections.Generic;
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
            return View(db.Summaries.ToList());
        }

        // GET: Doctor/Details/5
        public ActionResult Details(int? id)
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


    }
}