. assert.sh

# 1) проверка на успешный запуск сервера(x11vnc), с портом по умолчанию '5900'
assert "../x11vnc -forever -shared -bg" "PORT=5900"

# 2) проверка на наличие запущенного сервера в списке процессов
assert "netstat -na | grep LIST | grep 5900 | wc -l" "2"

# 3) запуск клиентов (vncviewer) и проверка на их наличие в списке процессов
assert "./pull_clients.sh ; netstat -na | grep ESTABLISHED | grep 5900 | wc -l ; exit ; kill -9 $(pidof vncviewer) ; netstat -na | grep ESTABLISHED | grep vncviewer | wc -l" "4"

# 4) закрыть все запущенные сервера (x11vnc)
assert "kill -9 $(pidof x11vnc);
	netstat -na | grep LIST | grep 5900 | wc -l" "0"

# end of test suite
assert_end examples
