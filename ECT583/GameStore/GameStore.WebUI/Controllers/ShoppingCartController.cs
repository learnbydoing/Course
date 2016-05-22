using GameStore.Domain.Infrastructure;
using GameStore.Domain.Model;
using GameStore.WebUI.Models;
using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace GameStore.WebUI.Controllers
{
    public class ShoppingCartController : Controller
    {
        // GET: ShoppingCart
        public ActionResult Index()
        {
            ShoppingCart cart = (ShoppingCart)Session["ShoppingCart"];
            if (cart == null)
            {
                cart = new ShoppingCart();
                Session["ShoppingCart"] = cart;
            }
            return View(cart);
        }

        public ActionResult Post([FromBody]CartViewModel value)
        {
            ShoppingCart cart = (ShoppingCart)Session["ShoppingCart"];
            if (cart == null)
            {
                cart = new ShoppingCart();
                Session["ShoppingCart"] = cart;
            }
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                Product product = context.Products.Find(value.Id);
                if (product != null)
                {
                    if (value.Quantity <= 0)
                    {
                        cart.AddItem(value.Id, product);
                    }
                    else
                    {
                        cart.SetItemQuantity(value.Id, value.Quantity, product);
                    }
                }
            }
            
            return View("Index", cart);
        }

        public ActionResult Submit()
        {
            ShoppingCart cart = (ShoppingCart)Session["ShoppingCart"];
            if (cart == null)
            {
                cart = new ShoppingCart();
                Session["ShoppingCart"] = cart;
                return View("Index", cart);
            }
            using (GameStoreDBContext context = new GameStoreDBContext())
            {
                Order newOrder = context.Orders.Create();
                newOrder.Address = "1 est jas";
                newOrder.CreditCard = "313131321";
                newOrder.DeliveryDate = DateTime.Now;
                newOrder.ConfirmationNumber = "1 est jas11111";
                newOrder.UserId = User.Identity.GetUserId();                
                context.Orders.Add(newOrder);
                context.SaveChanges();

                cart.GetItems().ForEach(c => context.OrderItems.Add(new OrderItem { OrderId = newOrder.OrderId, ProductId = c.GetItemId(), Quantity = c.Quantity }));
                System.Web.HttpContext.Current.Cache.Remove("OrderList");
                Session["ShoppingCart"] = null;
            }
            return RedirectToAction("Index", "Home");
        }
    }
}