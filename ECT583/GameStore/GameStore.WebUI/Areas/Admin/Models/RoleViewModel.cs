using GameStore.Domain.Identity;
using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameStore.WebUI.Areas.Admin.Models
{
    public class RoleViewModel: AppRole
    {
        public Boolean CanAdd { get; set; }
        public String Title { get; set; }
        public String StatusMessage { get; set; }
        public bool IsErrorStatusMessage { get; set; }
    }
}