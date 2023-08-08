function createHeader(title) {
    const element = document.querySelector(".header_container");

    element.innerHTML = '<div class="row1">\n' +
        '            <div class="logo">\n' +
        '                <a href="/"><img src="/resources/img/welcome_page_logo.png" alt="" width="94px" height="88px"></a>\n' +
        '            </div>\n' +
        '            <div class="title">'+ title +'</div>\n' +
        '        </div>\n' +
        '        <div class="row2">\n' +
        '            <div class="columns__row">\n' +
        '                <div class="column_item">\n' +
        '                    <div class="item__ref data">\n' +
        '                        <a href="/peers/">Data\n' +
        '                            <ul class="submenu">\n' +
        '                                <li><a href="/peers?limit=30&offset=0">Peers</a></li>\n' +
        '                                <li><a href="#">Tasks</a></li>\n' +
        '                                <li><a href="#">Checks</a></li>\n' +
        '                                <li><a href="#">Verter</a></li>\n' +
        '                                <li><a href="#">XP</a></li>\n' +
        '                                <li><a href="#">P2P</a></li>\n' +
        '                                <li><a href="#">TransferredPoints</a></li>\n' +
        '                                <li><a href="#">Friends</a></li>\n' +
        '                                <li><a href="#">Recommendations</a></li>\n' +
        '                                <li><a href="#">TimeTracking</a></li>\n' +
        '                            </ul>\n' +
        '                        </a>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <div class="column_item">\n' +
        '                    <div class="item__ref">\n' +
        '                        <a href="/peers?limit=30&offset=0">Operations</a>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </div>'
}