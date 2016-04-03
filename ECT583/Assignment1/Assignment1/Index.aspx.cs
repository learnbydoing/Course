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
            if (!IsPostBack)
            {
                // Update status for each time refresh the current page(F5 from browser or click Refresh button)
                Refresh();
            }
        }

        /// <summary>
        /// Handle the button click event
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected void btnRefresh_Click(object sender, EventArgs e)
        {
            // Use redirect instead of calling refresh methond to avoid 'confirm form resubmission' warning 
            // message. It will prompt if you refresh(F5) your browser after clicking the 'Refresh' button.
            Response.Redirect("Index.aspx");
            //Refresh();
        }
        
        /// <summary>
        /// Update the status
        /// </summary>
        private void Refresh()
        {
            // The session value is initialized in Session_Start event

            // Plus one for each visit
            Session["VisitedTimes"] = (int)Session["VisitedTimes"] + 1;
            // Convert to integer
            int visitedtimes = ((int)Session["VisitedTimes"]);
            // Update visited times to label in html
            lblVisitedTimes.Text = visitedtimes.ToString();
            // Show proper message to page according to the visited times
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
                lblMessage.Text = "Be patient...";
            }
            else // > 100
            {
                lblMessage.Text = "Leave me alone!";
            }
        }
    }
}