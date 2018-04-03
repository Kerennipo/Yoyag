
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data.Entity;
using System.Linq;
using System.Web;


namespace FinalProject.Models
{
    public class Patient
    {
        public int ID { get; set; }
        public string token { get; set; }
        public string customerID { get; set; }
        public DateTime timeStamp { get; set; }
        [DisplayName("Body Tempreture")]
        public double bodyTempreture { get; set; }
        [DisplayName("Head Ache")]
        public bool headAche { get; set; }
        [DisplayName("Stomach Ache")]
        public bool stomachAche { get; set; }
        [DisplayName("Sore Throat")]
        public bool soreThroat { get; set; }
        [DisplayName("Ears Ache")]
        public bool earsAche { get; set; }
        [DisplayName("Urinal Pain")]
        public bool urinalPain { get; set; }
        [DisplayName("Diarriah")]
        public bool diarriah { get; set; }
        [DisplayName("Vomiting")]
        public bool vomiting { get; set; }
        [DisplayName("Pregnancy")]
        public bool pregnancy { get; set; }
        [DisplayName("Shortness of Breath")]
        public bool shortnessOfBreath { get; set; }
        [DisplayName("Running Nose")]
        public bool runningNose { get; set; }
        [DisplayName("Sore Muscles")]
        public bool soreMuscles { get; set; }
        [DisplayName("Chest Pain")]
        public bool chestPain { get; set; }
        [DisplayName("Fatigue")]
        public bool fatigue { get; set; }
        [DisplayName("Weight Loss")]
        public bool weightLoss { get; set; }
        [DisplayName("Dizzyness")]
        public bool dizzyness { get; set; }
        [DisplayName("Loss Of Apetite")]
        public bool lossOfApetite { get; set; }

    }

    public class PatientDBContext : DbContext
    {
        public DbSet<Patient> Patients { get; set; }
    }
}