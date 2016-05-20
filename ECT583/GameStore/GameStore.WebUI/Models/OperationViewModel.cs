using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameStore.WebUI.Models
{
    public class OperationViewModel
    {
        public int Id { get; set; }
        public string Message { get; set; }
        public bool IsError { get; set; }
    }
}