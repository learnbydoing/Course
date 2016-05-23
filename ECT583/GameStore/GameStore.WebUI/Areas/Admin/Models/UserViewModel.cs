using GameStore.Domain.Identity;
using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GameStore.WebUI.Areas.Admin.Models
{
    public class UserViewModel: AppUser
    {
        public Boolean CanAdd { get; set; }

        [Required]
        [EmailAddress]
        [Display(Name = "Email")]
        public override string Email { get; set; }

        [Required]
        [Display(Name = "PhoneNumber")]
        public override string PhoneNumber { get; set; }

        [Required]
        [Display(Name = "Membership")]
        public override string Membership { get; set; }
    }
}