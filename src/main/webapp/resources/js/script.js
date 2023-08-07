var params = window
    .location
    .search
    .replace('?','')
    .split('&')
    .reduce(
        function(p,e){
            var a = e.split('=');
            p[ decodeURIComponent(a[0])] = decodeURIComponent(a[1]);
            return p;
        },
        {}
    );

const selector = document.querySelector('.auto-send-select');
selector.value = params['limit'];

selector.addEventListener('change', e => {
    const select = e.target;
    window.location.href = `/peers?limit=${select.value}&offset=${0}`;
});
