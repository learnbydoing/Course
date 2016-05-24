using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Configuration;

namespace GameStore.WebUI.Helper
{
    public class ConfigurationHelper
    {
        public static string GetDefaultPassword()
        {
            return WebConfigurationManager.AppSettings["configFile"];
        }
    }
}