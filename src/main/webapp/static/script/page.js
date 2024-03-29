function nextPage() {
	const totalPage=document.getElementsByClassName("page").length;
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
function changePage(e){
	console.log(e.innerText);
    updatePageParameter(e.innerText);
}

const focused = document.querySelector('.activePage')
focused.scrollIntoView({ behavior: 'instant' })