function hide(id) {
    var table = document.getElementById(id);
    var hidden = "hidden";
    if (table.getAttribute(hidden) == null)
        table.setAttribute(hidden, "hidden");
    else table.removeAttribute(hidden);
}