
import moment from 'moment';
export function transDateFrom(date){
    return moment(date).format("YYYY[年] MMMM Do ,dddd, HH:mm")
}