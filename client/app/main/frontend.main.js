


var swimmers = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    local: [
        { name: "חיילה מישמושקוביץ", image: '/tmp/swimmer1.jpg' },
        { name: "רבקה אמנו" },
        { name: "מושקה דינה" },
        { name: "דודינקה ישראלוביץ"},
        { name: "שורלה קרוקשבילי" }
    ]
});

$(function() {
    swimmers.initialize();
    $('#swimmer-select-checkbox').change(
        function () {
            $('#swimmer-select').toggle();
        }
    );
});

$('#swimmer-select .typeahead').typeahead({
        hint: true,
        highlight: true
    },
    {
    name: 'swimmers',
    displayKey: 'name',
    source: swimmers.ttAdapter(),
    templates: {
        empty: [
            '<div class="empty-message">',
                'לא נמצאה צולחת בשם זה',
            '</div>'
        ].join('\n'),
        suggestion: function(swimmer) {
                        var img = swimmer.image ? swimmer.image : '/tmp/monkey.jpg';
                        var html = '<div class="alert alert-info">';
                        html += '<div class="pull-right"><img src="' + img + '" width="48" height="48" class="img-rounded"></div>';
                        html += '<div class="pull-right col-lg-offset-1">';
                        html += '<div class="text-right"><strong>' + swimmer.name + '</strong></div>';
                        html += '<div class="text-right">' + 'כאן ושם' + '</div>';
                        html += '</div>';
                        html += '<div class="clearfix"></div>';
                        html += '</div>';
                        return html;
                    }
    }
});