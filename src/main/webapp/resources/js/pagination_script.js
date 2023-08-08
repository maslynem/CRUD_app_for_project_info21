let params = window
    .location
    .search
    .replace('?', '')
    .split('&')
    .reduce(
        function (p, e) {
            let a = e.split('=');
            p[decodeURIComponent(a[0])] = decodeURIComponent(a[1]);
            return p;
        },
        {}
    );

function onSelectionChange(select) {
    let selectedOption = select.options[select.selectedIndex];
    window.location.href = `/peers?limit=${selectedOption.value}&offset=${0}`;
}

function createSelector(totalPages, currentPage) {
    const element = document.querySelector(".selector");

    element.innerHTML = '<div class="item_text">Строк на странице</div> ' +
        '<select class=auto-send-select data-action=peers name=select onchange="onSelectionChange(this)">' +
        '<option>30</option>' +
        '<option>50</option>' +
        '<option>100</option>' +
        '</select>' +
        '<div class="item_text"> Страница: ' + currentPage + ' из ' + totalPages + '</div>';

    const selector = document.querySelector('.auto-send-select');
    selector.value = params['limit'];
}


function createPagination(limit, totalPages, page, path) {
    const element = document.querySelector(".pagination ul");

    let liTag = '';
    let active;
    let beforePage = page - 1;
    let afterPage = page + 1;
    if (page > 1) { //show the next button if the page value is greater than 1
        let url = path + '?limit=' + limit + '&offset=' + (page - 2) + '';
        liTag += `<li class="btn prev" onclick="window.location.href = '${url}'"><span><i class="fas fa-angle-left"></i> Prev</span></li>`;
    }

    if (page > 2) { //if page value is less than 2 then add 1 after the previous button
        let url = path + '?limit=' + limit + '&offset=0';
        liTag += `<li class="first numb" onclick="window.location.href = '${url}'"><span>1</span></li>`;
        if (page > 3) { //if page value is greater than 3 then add this (...) after the first li or page
            liTag += `<li class="dots"><span>...</span></li>`;
        }
    }

    // how many pages or li show before the current li
    if (page == totalPages) {
        beforePage = beforePage - 2;
    } else if (page == totalPages - 1) {
        beforePage = beforePage - 1;
    }
    // how many pages or li show after the current li
    if (page == 1) {
        afterPage = afterPage + 2;
    } else if (page == 2) {
        afterPage = afterPage + 1;
    }

    for (var plength = beforePage; plength <= afterPage; plength++) {
        if (plength > totalPages) { //if plength is greater than totalPage length then continue
            continue;
        }
        if (plength == 0) { //if plength is 0 than add +1 in plength value
            plength = plength + 1;
        }
        if (page == plength) { //if page is equal to plength than assign active string in the active variable
            active = "active";
        } else { //else leave empty to the active variable
            active = "";
        }
        let url = path + '?limit=' + limit + '&offset=' + (plength - 1) + '';
        liTag += `<li class="numb ${active}" onclick="window.location.href = '${url}'"><span>${plength}</span></li>`;
    }

    if (page < totalPages - 1) { //if page value is less than totalPage value by -1 then show the last li or page
        if (page < totalPages - 2) { //if page value is less than totalPage value by -2 then add this (...) before the last li or page
            liTag += `<li class="dots"><span>...</span></li>`;
        }
        let url = path + '?limit=' + limit + '&offset=' + (totalPages - 1) + '';
        liTag += `<li class="last numb" onclick="window.location.href = '${url}'"><span>${totalPages}</span></li>`;
    }

    if (page < totalPages) { //show the next button if the page value is less than totalPage(20)
        let url = path + '?limit=' + limit + '&offset=' + page + '';
        liTag += `<li class="btn next" onclick="window.location.href = '${url}'"><span>Next <i class="fas fa-angle-right"></i></span></li>`;
    }
    element.innerHTML = liTag; //add li tag inside ul tag
}

