using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Assignment3.Models
{
    public class CustomerDeleteViewModel
    {
        public int Id { get; set; }
        public string Message { get; set; }
        public bool IsError { get; set; }
    }
}