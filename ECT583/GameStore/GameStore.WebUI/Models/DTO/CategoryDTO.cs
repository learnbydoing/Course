using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GameStore.WebUI.Models.DTO
{
    public class CategoryDTO
    {
        [Display(Name = "Category Id")]
        [Required]
        public int CategoryId { get; set; }        
        [Display(Name = "Category Name")]
        [Required]
        public string CategoryName { get; set; }
    }
}