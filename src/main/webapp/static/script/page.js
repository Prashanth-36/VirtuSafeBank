function nextPage() {
	const totalPage=decument.getElementByClassName("page").length;
	const currentPage = document.querySelector(".activePage").textContent;
	if(currentPage<totalPage){
  		updatePageParameter(+currentPage + 1);
  	}
}
function previousPage() {
  const currentPage = document.querySelector(".activePage").textContent;
  if (currentPage > 1) {
  	updatePageParameter(+currentPage - 1);
  }
}
function updatePageParameter(pageNumber) {
  const queryParam = new URLSearchParams(window.location.search);
  queryParam.set("page", pageNumber);
  window.location.search = queryParam;
}
const pages = document.querySelectorAll(".page");
pages.forEach((p) => {
  p.addEventListener("click", (e) => {
    console.log(this.textContent);
    updatePageParameter(e.target.textContent);
  });
});
