using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GameStore.WebUI.Models
{
    public class CheckoutViewModel
    {
        [Required]
        public string Address { get; set; }
        [Required]
        public string CreditCard { get; set; }
    }
}