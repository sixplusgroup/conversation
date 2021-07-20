import sys
import xlrd
from xlutils.copy import copy
import time
import operator


def custom_time(timestamp):
    time_local = time.localtime(timestamp/1000)
    dt = time.strftime("%Y-%m-%d %H:%M:%S", time_local)
    return dt


def query_history_data(argv):
    target = argv[1]
    template = xlrd.open_workbook('template.xlsx')
    excel = copy(template)
    table = excel.get_sheet(0)
    row = 1
    targetlist, datetime = [], []
    for i in range(2, len(argv)):
        try:
            with open(argv[i], 'r', encoding='utf-8') as f:
                for line in f:
                    data = line.split(",")
                    if data[2] == target and data[-1] not in datetime:
                        targetlist.append(data)
                        datetime.append(data[-1])
        except:
            if argv[i][-4:] != ".csv":
                print("文件格式不正确")
            else:
                print("未找到指定文件"+"\"" + argv[i] + "\"")
            pass
    if len(targetlist) == 0:
        print("未找到数据")
        return
    targetlist.sort(key=operator.itemgetter(-1))
    for j in range(len(targetlist)):
        for k in range(len(targetlist[0])-1):
            table.write(row, k, targetlist[j][k])
        table.write(row, len(targetlist[0])-1, custom_time(int(targetlist[j][-1])))
        row += 1
    excel.save(target + ".xls")


if __name__ == "__main__":
    argv = sys.argv
    if len(argv) == 1:
        print("未指定machine_id和文件")
    elif len(argv) == 2:
        print("未指定查询文件")
    elif len(argv[1]) != 12:
        print("machine_id格式不正确")
    elif "." in argv[1]:
        print("输入顺序有误或未输入machine_id")
    else:
        query_history_data(argv)
