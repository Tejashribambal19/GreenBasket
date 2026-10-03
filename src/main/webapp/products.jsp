<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,com.mycompany.greenbasket.*" %>
<%
ProductDAO pd=new ProductDAO();
Integer cat=null; try{cat=Integer.valueOf(request.getParameter("category"));}catch(Exception ignored){}
String q=request.getParameter("q");
List<Category> cats=pd.getCategories();
List<Product> products=pd.getAllActive(cat,q);
Map<Integer,String[]> ratingMap=new FeedbackDAO().getRatingSummary();
%>
<!doctype html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Shop Organic | GreenBasket</title><link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/greenbasket.css"></head><body>
<%@ include file="/WEB-INF/includes/header.jspf" %>
<main class="page-shell"><div class="container">
<div class="page-head"><span class="eyebrow">Organic marketplace</span><h1 style="margin-top:14px">Fresh finds from GreenBasket farmers.</h1><p>Search by product, farm or category. Every listing belongs to GreenBasket's organic-only catalog.</p></div>
<form class="catalog-toolbar" method="get"><div class="searchbar"><input name="q" value="<%=q==null?"":q%>" placeholder="Search tomatoes, honey, farm name..."><select name="category"><option value="">All categories</option><%for(Category c:cats){%><option value="<%=c.getId()%>" <%=cat!=null&&cat==c.getId()?"selected":""%>><%=c.getIcon()%> <%=c.getName()%></option><%}%></select><button class="btn btn-primary">Search</button></div></form>
<%if(products.isEmpty()){%><div class="empty"><div style="font-size:44px">🌱</div><h3>No products found yet</h3><p>Try another category or check back after farmers add new listings.</p></div><%}else{%>
<div class="products-grid">
<%for(Product p:products){
  String st=pd.verificationStatus(p.getFarmerId());
  String[] rate=ratingMap.get(p.getId());
  String src=ProductImageUtil.resolveForWeb(request.getContextPath(),p.getImageUrl(),p.getName(),p.getCategoryName());
  String localFallback=ProductImageUtil.localFallbackForWeb(request.getContextPath(),p.getName(),p.getCategoryName());
%>
<article class="product-card">
  <div class="product-media"><img src="<%=src%>" alt="<%=p.getName()%>" loading="lazy" data-local-fallback="<%=localFallback%>" onerror="greenBasketImageFallback(this)"></div>
  <div class="product-body">
    <div class="product-meta"><span><%=p.getCategoryName()%></span><span><%=p.getStock()%> <%=p.getUnit()%> available</span></div>
    <h3><%=p.getName()%></h3>
    <%if(rate!=null){%><div class="product-rating"><span class="rating-stars">★★★★★</span><strong><%=rate[0]%></strong><span>(<%=rate[1]%> review<%="1".equals(rate[1])?"":"s"%>)</span></div><%}else{%><div class="product-rating muted-rating">New · no reviews yet</div><%}%>
    <p><%=p.getDescription()==null?"Fresh organic produce from a GreenBasket farm.":p.getDescription()%></p>
    <div style="margin-bottom:12px;font-size:12px;color:#53665a"><strong><%=p.getFarmName()%></strong> · <%=p.getFarmerName()%> <%if("APPROVED".equals(st)){%><span class="verified-inline"> ✓ Verified</span><%}%></div>
    <div class="price-row"><div class="price">₹<%=p.getPrice()%> <small>/ <%=p.getUnit()%></small></div><form action="CartServlet" method="post"><input type="hidden" name="productId" value="<%=p.getId()%>"><input type="hidden" name="quantity" value="1"><button class="btn btn-primary" <%=p.getStock()<=0?"disabled":""%>><%=p.getStock()>0?"Add to cart":"Sold out"%></button></form></div>
  </div>
</article>
<%}%>
</div><%}%>
</div></main>
<script>
function greenBasketImageFallback(img){
  img.onerror=null;
  if(img.dataset.localFallback && img.src !== img.dataset.localFallback){
    img.src=img.dataset.localFallback;
  }
}
</script>
<%@ include file="/WEB-INF/includes/footer.jspf" %></body></html>
