using Newtonsoft.Json;
using System;
using System.IO;
using System.Net;
using System.Text;
using System.Web.Mvc;

namespace FinalProject.Models
{
    public class webproxy
    {
        private const string postURL = "http://193.106.55.122/post";
        private const string updateURL = "http://193.106.55.122/update";
        private const string statisticsURL = "http://193.106.55.122/getStatistics";

        public HttpStatusCodeResult patientSubmit(string token, string userID, string timestamp, string data)
        {
            var json = GetJson(token, userID, timestamp, data);
            try
            {
                var httpWebRequest = (HttpWebRequest)WebRequest.Create(postURL);
                httpWebRequest.ContentType = "application/json";
                httpWebRequest.Method = "POST";

                using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
                {

                    streamWriter.Write(json);
                    streamWriter.Flush();
                    streamWriter.Close();
                }

                var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
                if (httpResponse.StatusCode == HttpStatusCode.NoContent)
                    return new HttpStatusCodeResult(200);
                httpResponse.Dispose();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return new HttpStatusCodeResult(500);
            }
            return new HttpStatusCodeResult(200);
        }

        public HttpStatusCodeResult update(string token, string userID, string sessionID, string formData)
        {

            var json = GetDiagnosisJson(token, userID, sessionID, formData);
            try
            {
                var httpWebRequest = (HttpWebRequest)WebRequest.Create(updateURL);
                httpWebRequest.ContentType = "application/json";
                httpWebRequest.Method = "POST";

                using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
                {

                    streamWriter.Write(json);
                    streamWriter.Flush();
                    streamWriter.Close();
                }

                var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
                if (httpResponse.StatusCode == HttpStatusCode.NoContent)
                    return new HttpStatusCodeResult(200);
                httpResponse.Dispose();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return new HttpStatusCodeResult(500); ;
            }
            return new HttpStatusCodeResult(200);
        }

        public string getStats(string token, string userID, string sessionID)
        {
            var json = GetStatsJson(token, userID, sessionID);
            var response = string.Empty;
            try
            {
                var httpWebRequest = (HttpWebRequest)WebRequest.Create(statisticsURL);
                httpWebRequest.ContentType = "application/json";
                httpWebRequest.Method = "POST";
                httpWebRequest.Accept = "application/json; charset=utf-8";

                using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
                {

                    streamWriter.Write(json);
                    streamWriter.Flush();
                    streamWriter.Close();
                }

                var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
                var encoding = ASCIIEncoding.UTF8;
                using (var streamReader = new StreamReader(httpResponse.GetResponseStream(),encoding))
                {
                    response = streamReader.ReadToEnd();
                    streamReader.Close();
                    streamReader.Dispose();
                }
                httpResponse.Dispose();
                return response;
                
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return "false" ;
            }
        }


        private static string GetJson(string token, string userID, string timestamp, string data)
        {
            return JsonConvert.SerializeObject(new ForwardedData()
            {
                data = new Text()
                {
                    text = data
                },
                token = token,
                userID = userID,
                timestamp = timestamp
            });
        }

        private static string GetDiagnosisJson(string token, string userID, string sessionID, string formData)
        {
            return JsonConvert.SerializeObject(new ForwardedDiagnosis()
            {
                token = token,
                userID = userID,
                sessionID = sessionID,
                formData= formData,
            });
        }

        private static string GetStatsJson(string token, string userID, string sessionID)
        {
            return JsonConvert.SerializeObject(new ForwardedStats()
            {
                token = token,
                userID = userID,
                sessionID = sessionID,
            });
        }
    }
}