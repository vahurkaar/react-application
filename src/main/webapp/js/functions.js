function getSectors(sectors, level) {
    var result = [];
    $.each(sectors, function(key, sector) {
        sector.styleClass = 'option-level-' + level;
        result.push(sector);
        if (sector.children.length > 0) {
            result = result.concat(getSectors(sector.children, level + 1));
        }
    });

    return result;
}

function getChangeValue(event) {
    if (event.target.type == 'select-multiple') {
        var selected = [];
        $.each(event.target.options, function(key, option) {
            if (option.selected) {
                selected.push(option.value);
            }
        });
        return selected;
    } else if (event.target.type == 'checkbox') {
        return event.target.checked;
    } else {
        return event.target.value;
    }
}