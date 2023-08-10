function createHeader(title) {
    const element = document.querySelector("header");

    element.innerHTML = '<div class="row1">\n' +
        '            <div class="logo">\n' +
        '                <a href="/"><img src="/resources/img/welcome_page_logo.png" alt="" width="94px" height="88px"></a>\n' +
        '            </div>\n' +
        '            <div class="header__text header__title">'+ title +'</div>\n' +
        '        </div>\n' +
        '        <div class="row2">\n' +
        '                <div class="column_item">\n' +
        '                    <div class="item__ref data__submenu">\n' +
        '                        <a class = "header__text" href="/peers/page-0?pageSize=30">Data\n' +
        '                            <ul class="data__list_submenu">\n' +
        '                                <li><a class = "header__text" href="/peers/page-0?pageSize=30">Peers</a></li>\n' +
        '                                <li><a class = "header__text" href="/tasks/page-0?pageSize=30">Tasks</a></li>\n' +
        '                                <li><a class = "header__text" href="#">Checks</a></li>\n' +
        '                                <li><a class = "header__text" href="#">Verter</a></li>\n' +
        '                                <li><a class = "header__text" href="#">XP</a></li>\n' +
        '                                <li><a class = "header__text" href="#">P2P</a></li>\n' +
        '                                <li><a class = "header__text" href="#">TransferredPoints</a></li>\n' +
        '                                <li><a class = "header__text" href="#">Friends</a></li>\n' +
        '                                <li><a class = "header__text" href="#">Recommendations</a></li>\n' +
        '                                <li><a class = "header__text" href="#">TimeTracking</a></li>\n' +
        '                            </ul>\n' +
        '                        </a>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <div class="column_item">\n' +
        '                    <div class="item__ref">\n' +
        '                        <a class = "header__text" href="#">Operations</a>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '        </div>'
}