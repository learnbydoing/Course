using GameStore.Domain.Model;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GameStore.WebUI.Models.DTO
{
    public class ProductDTO : Product
    {
        public string CategoryName { get; set; }
    }
}