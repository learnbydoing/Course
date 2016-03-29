using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Assignment1
{
    public partial class Index : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            
            Session["VisitedTimes"] = (int)Session["VisitedTimes"] + 1;
            int visitedtimes = ((int)Session["VisitedTimes"]);
            lblVisistedTimes.Text = visitedtimes.ToString();
            if (visitedtimes == 1)
            {
                lblMessage.Text = "Welcome to the site!";
            }
            else if (visitedtimes >= 2 && visitedtimes <= 10)
            {
                lblMessage.Text = "Thanks for coming back!";

            }
            else if (visitedtimes >= 11 && visitedtimes <= 50)
            {
                lblMessage.Text = "Ummm… not much going on here, feel free to visit other sites.";
            }
            else if (visitedtimes >= 51 && visitedtimes <= 100)
            {
                lblMessage.Text = "Be patient..";
            }
            else // > 100
            {
                lblMessage.Text = "Leave me alone";
            }            
        }
    }
}