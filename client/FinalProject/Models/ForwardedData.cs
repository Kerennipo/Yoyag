using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace FinalProject.Models
{
    public class ForwardedData
    {
        public string token { get; set; }
        public string userID { get; set; }
        public string timestamp { get; set; }
        public Text data { get; set; }
    }
}