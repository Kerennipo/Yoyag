namespace FinalProject.Models
{
    internal class ForwardedDiagnosis
    {
        public ForwardedDiagnosis()
        {
        }

        public string token { get; set; }
        public string userID { get; set; }
        public string sessionID { get; set; }
        public string formData { get; set; }
    }
}