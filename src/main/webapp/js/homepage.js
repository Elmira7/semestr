function showCatalog(){
    if($('.catalog_holder').css('height') == '0px'){
        $('.catalog_holder').css('height', 'auto');
    } else {
        $('.catalog_holder').css('height', '0px');
    }
}

$(document).mouseup( function(e){
    var div = $('.catalog_holder');
    if ( !div.is(e.target)
        && div.has(e.target).length === 0 ) {
        $('.catalog_holder').css('height', '0px');
    }
});

