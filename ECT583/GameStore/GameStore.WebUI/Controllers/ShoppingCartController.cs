using GameStore.Domain.Infrastructure;
using GameStore.Domain.Model;
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

        public ActionResult CreateOrUpdate(CartViewModel value)
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

            Session["CartCount"] = cart.GetItems().Count();
            return View("Index", cart);
        }

        public ActionResult Checkout()
        {
            CheckoutViewModel checkout = new CheckoutViewModel();
            checkout.Address = "1st Jackson Ave,Chicago,IL";
            checkout.CreditCard = "3101122033287498";
            return View(checkout);
        }

        public ActionResult PlaceOrder(CheckoutViewModel value)
        {
            ShoppingCart cart = (ShoppingCart)Session["ShoppingCart"];
            if (cart == null)
            {
                ViewBag.Message = "Your cart is empty!";
                return View("Index", "ShoppingCart");
            }
            if (String.IsNullOrEmpty(value.Address) || String.IsNullOrEmpty(value.CreditCard))
            {
                ViewBag.Message = "Please provide address and credit card!";
                return View("Checkout", "ShoppingCart");
            }

            OrderViewModel model = new OrderViewModel();
            try
            {
                using (GameStoreDBContext context = new GameStoreDBContext())
                {
                    Order newOrder = context.Orders.Create();
                    newOrder.Address = value.Address;
                    newOrder.CreditCard = value.CreditCard;
                    newOrder.DeliveryDate = DateTime.Now.AddDays(14);
                    newOrder.ConfirmationNumber = value.CreditCard.Substring(value.CreditCard.Length - 4) + DateTime.Now.ToString("yyyyMMddHHmmss");
                    newOrder.UserId = User.Identity.GetUserId();
                    context.Orders.Add(newOrder);
                    cart.GetItems().ForEach(c => context.OrderItems.Add(new OrderItem { OrderId = newOrder.OrderId, ProductId = c.GetItemId(), Quantity = c.Quantity }));
                    context.SaveChanges();
                    System.Web.HttpContext.Current.Cache.Remove("OrderList");
                    Session["ShoppingCart"] = null;
                    Session["CartCount"] = 0;
                    Session["OrderCount"] = (int)Session["OrderCount"] + 1;

                    var order = from o in context.Orders
                                join u in context.Users
                                  on o.UserId equals u.Id
                               where o.OrderId == newOrder.OrderId
                              select new { o.OrderId, o.UserId, u.UserName, o.Address, o.CreditCard, o.ConfirmationNumber, o.DeliveryDate };
                    var ord = order.FirstOrDefault();
                    model = new OrderViewModel { OrderId = ord.OrderId, UserId = ord.UserId, UserName = ord.UserName, Address = ord.Address, CreditCard = ord.CreditCard, ConfirmationNumber = ord.ConfirmationNumber, DeliveryDate = ord.DeliveryDate };

                    var orderitems = from i in context.OrderItems
                                     join p in context.Products
                                       on i.ProductId equals p.ProductId
                                     join c in context.Categories
                                       on p.CategoryId equals c.CategoryId
                                     where i.OrderId == newOrder.OrderId
                                    select new { i.OrderItemId, i.OrderId, i.ProductId, p.ProductName, p.CategoryId, c.CategoryName, p.Price, p.Image, p.Condition, p.Discount, i.Quantity};
                    model.Items = orderitems.Select(o => new OrderItemViewModel { OrderItemId = o.OrderItemId, OrderId = o.OrderId, ProductId = o.ProductId, ProductName = o.ProductName, CategoryId = o.CategoryId, CategoryName = o.CategoryName, Price = o.Price, Image = o.Image, Condition = o.Condition, Discount = o.Discount, Quantity = o.Quantity }).ToList();
                }
            }
            catch(Exception ex)
            {
                ViewBag.Message = "Error Occurs:"+ ex.Message;
            }

            return View(model);
        }
    }
}