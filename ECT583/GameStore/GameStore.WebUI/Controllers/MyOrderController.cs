using GameStore.Domain.Infrastructure;
using GameStore.WebUI.Models;
using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GameStore.WebUI.Controllers
{
    [Authorize]
    public class MyOrderController : Controller
    {
        // GET: MyOrder
        public ActionResult Index()
        {
            List<OrderViewModel> list = new List<OrderViewModel>();
            try
            {
                String userid = User.Identity.GetUserId();
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    var orders = from o in context.Orders
                                 join u in context.Users
                                   on o.UserId equals u.Id
                                 where o.UserId == userid
                                 select new { o.OrderId, o.UserId, u.UserName, o.Address, o.CreditCard, o.ConfirmationNumber, o.DeliveryDate };
                    list = orders.Select(o => new OrderViewModel { OrderId = o.OrderId, UserId = o.UserId, UserName = o.UserName, Address = o.Address, CreditCard = o.CreditCard, ConfirmationNumber = o.ConfirmationNumber, DeliveryDate = o.DeliveryDate }).ToList();
                }
            }
            catch (Exception ex)
            {
                ViewBag.Message = "Error Occurs:" + ex.Message;
            }

            return View(list);
        }

        public ActionResult Detail()
        {
            return View();
        }
    }
}