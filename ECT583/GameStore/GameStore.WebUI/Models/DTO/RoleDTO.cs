using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GameStore.WebUI.Models.DTO
{
    public class RoleDTO
    {
        [Display(Name = "Role Id")]
        [Required]
        public string RoleId { get; set; }        
        [Display(Name = "Role Name")]
        [Required]
        public string RoleName { get; set; }        
        public string Description { get; set; }
    }
}