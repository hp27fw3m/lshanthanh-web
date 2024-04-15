function updatePassportIssuedPlace(pNumber, pIssuedPlace) {
    if (pNumber.value.length == 0) {
        pIssuedPlace.value = "";
    }
    if (pNumber.value.length == 8 || pNumber.value.length == 9) {
        switch (pNumber.value[0].toUpperCase()) {
            case "N":
            case "Q":
                pIssuedPlace.value = "ĐSQ Việt Nam tại Hàn Quốc";
                break;
            case "C":            
            case "K":
                pIssuedPlace.value = processCKPassport(pNumber.value.substring(0, 4));
                break;
            case "M":
                pIssuedPlace.value = "Bộ Ngoại giao Hàn Quốc";
                break;
            default:
                pIssuedPlace.value = "Cục Quản lý XNC, Bộ Công an";
                break;
        }
    }
}
function processCKPassport(prefix) {
    switch (prefix) {
        case "K014":
        case "K015":
        case "K047":
        case "C948":
            return "ĐSQ Việt Nam tại Hàn Quốc";
        default:
            return "Cục Quản lý XNC, Bộ Công an";

    }
}