/* ===== АВТОРИЗАЦИЯ ===== */
add('splash','auth','Запуск · восстановление сессии','Показывается до 1 с, пока проверяется refresh-токен в Keychain. Сессия валидна → сразу персональная афиша (сценарий В). Токен истёк или отозван → приветствие с предложением войти снова.',
SCR('splash',`<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center" data-go="welcome"><img src="ph/logo.png" style="width:190px"><div style="margin-top:44px;display:flex;gap:6px"><i class="dot" style="background:var(--pink)"></i><i class="dot" style="background:#3A3A44"></i><i class="dot" style="background:#3A3A44"></i></div></div><div style="position:absolute;bottom:56px;left:0;right:0;text-align:center" class="t-cap">Минск</div>${HI}`));

add('welcome','auth','01 · Приветствие','Два способа входа: Google и собственный Email-аккаунт ВРЫВ. Никаких Apple ID, Telegram, VK. «Смотреть афишу» включает гостевой режим: публичные события видны, а сохранение, свайпы, чат и планы открывают окно входа.',
SCR('welcome',`<div class="ph" style="position:absolute;top:0;left:0;right:0;height:500px"><img src="${I('techno')}"><div style="position:absolute;inset:0;background:linear-gradient(180deg,rgba(11,11,15,.15) 0%,rgba(11,11,15,0) 35%,#0B0B0F 100%)"></div></div>
<div style="position:absolute;top:96px;left:0;right:0;display:flex;justify-content:center"><img src="ph/logo.png" style="width:136px"></div>
<div style="position:absolute;left:24px;right:24px;top:418px"><div class="t-display" style="font-size:38px">Город<br>происходит сейчас</div><div class="sub" style="font-size:16px;margin-top:12px">Находи события, людей и места, ради которых стоит выйти.</div></div>
<div style="position:absolute;left:16px;right:16px;bottom:44px">
<button class="btn white" data-go="gpick">${GOOGLE}Продолжить с Google</button>
<button class="btn sec" style="margin-top:10px" data-go="login">${ic('mail','style="width:20px;height:20px"')}Войти по Email</button>
<div style="display:flex;justify-content:space-between;margin-top:20px;padding:0 6px;font-size:15px;font-weight:600"><span class="link" data-go="reg">Создать аккаунт</span><span class="muted" data-go="guest">Смотреть афишу ${ic('chevR','style="width:15px;height:15px;display:inline;vertical-align:-3px"')}</span></div>
<div class="t-cap" style="font-size:11.5px;text-align:center;margin-top:18px;line-height:1.45">Продолжая, вы принимаете <u>Пользовательское соглашение</u><br>и <u>Политику конфиденциальности</u></div></div>${HI}`));

add('gpick','auth','Google · выбор аккаунта','Системное окно Google (ASWebAuthenticationSession). Запрашиваются только openid, email, profile. Аккаунт уже связан → сразу афиша. Новый → создаём профиль из имени, Email и фото и ведём на персонализацию. Email уже зарегистрирован через пароль → экран безопасного связывания.',
SCR('gpick',`<div class="ph" style="position:absolute;top:0;left:0;right:0;height:500px;opacity:.5"><img src="${I('techno')}"></div><div class="dim"></div>
<div class="bsheet" style="background:#1C1C22"><div class="grab"></div>
<div style="display:flex;align-items:center;gap:10px;justify-content:center">${GOOGLE}<span class="t-h3">Вход через Google</span></div>
<div class="t-cap" style="text-align:center;margin-top:6px">Приложение «ВРЫВ» получит имя, Email и фото профиля</div>
<div class="grp" style="margin-top:18px;background:#24242C">
<div class="li noi" data-go="p1" style="min-height:66px">${AV('av2',38)}<div><div style="font-size:15px">Максим Ковалёв</div><div class="t-cap" style="font-size:12.5px">max.kovalev@gmail.com</div></div><span class="v">${ic('chevR')}</span></div>
<div class="li noi" data-go="glink" style="min-height:66px"><div style="width:38px;height:38px;border-radius:50%;background:#3A3A44;display:flex;align-items:center;justify-content:center;font-weight:700">M</div><div><div style="font-size:15px">Максим К.</div><div class="t-cap" style="font-size:12.5px">m.kovalev.work@gmail.com</div></div><span class="v">${ic('chevR')}</span></div>
<div class="li noi"><div style="width:38px;height:38px;border-radius:50%;background:#26262E;display:flex;align-items:center;justify-content:center">${ic('plus','style="width:18px;height:18px"')}</div><span>Другой аккаунт</span></div></div>
<button class="btn ghost" style="height:44px;color:var(--text2)" data-go="welcome">Отмена</button></div>`));

add('glink','auth','Google · связывание аккаунтов','Google вернул Email, который уже зарегистрирован с паролем. Дубль не создаём и автоматически не объединяем: пользователь подтверждает владение паролем от ВРЫВ (или кодом на почту). После этого Google привязывается к существующему аккаунту.',
SCR('glink',`${NAV('',{back:'gpick'})}<div class="body">
<div style="display:flex;align-items:center;gap:-8px;margin-top:8px"><div style="width:60px;height:60px;border-radius:20px;background:var(--card);display:flex;align-items:center;justify-content:center"><img src="ph/logo.png" style="width:42px"></div><div style="width:36px;display:flex;justify-content:center;color:var(--text2)">${ic('link')}</div><div style="width:60px;height:60px;border-radius:20px;background:#F5F5F7;display:flex;align-items:center;justify-content:center">${GOOGLE.replace('width="20" height="20"','width="30" height="30"')}</div></div>
<div class="h-big" style="margin-top:24px">Этот Email уже<br>в ВРЫВ</div>
<div class="sub">Аккаунт <b style="color:var(--text)">m.kovalev.work@gmail.com</b> создан с паролем. Подтверди, что он твой, — и сможешь входить через Google.</div>
${FIELD('Пароль от ВРЫВ','••••••••••',{st:'focus',right:ic('eye')})}
<div style="text-align:right;margin-top:12px;font-size:14px" class="link" data-go="rec1">Не помню пароль — прислать код</div>
<div class="grp" style="margin-top:24px"><div class="li"><div class="lico">${ic('shield')}</div><div style="font-size:13.5px;color:var(--text2);font-weight:500;line-height:1.4;padding:12px 0">Профиль, друзья и планы сохранятся. Это один аккаунт с двумя способами входа.</div></div></div>
</div><div class="cta"><button class="btn pri" data-go="homeP">Связать и войти</button></div>${HI}`));

add('login','auth','02 · Вход по Email','Проверка полей на клиенте (формат Email, пустой пароль) до отправки. Глаз показывает/скрывает пароль. Кнопка неактивна, пока поля пусты. Неверная пара → общая ошибка без уточнения, что именно неверно. После 5 попыток — пауза 60 с.',
SCR('login',`${NAV('',{back:'welcome'})}<div class="body"><div class="h-big" style="margin-top:6px">С возвращением</div><div class="sub">Войди в аккаунт ВРЫВ</div>
${FIELD('Email','max.kovalev@gmail.com',{icon:'mail'})}
${FIELD('Пароль','••••••••••',{icon:'lock',st:'focus',right:ic('eye')})}
<div style="text-align:right;margin-top:14px;font-size:14px" class="link" data-go="rec1">Забыли пароль?</div>
<button class="btn pri" style="margin-top:22px" data-go="loginLoad">Войти</button>
<div class="or">или</div>
<button class="btn sec" data-go="gpick">${GOOGLE}Продолжить с Google</button>
</div><div style="position:absolute;bottom:44px;left:0;right:0;text-align:center;font-size:15px;color:var(--text2);font-weight:500">Нет аккаунта? <span class="link" data-go="reg">Создать</span></div>${HI}`),{states:['loginErr','loginLoad']});

add('loginErr','auth','02 · Вход — ошибка','Состояние ошибки: поля подсвечены розовым, сообщение под паролем. Email сохраняется, пароль очищается. Счётчик попыток виден только после третьей ошибки.',
SCR('loginErr',`${NAV('',{back:'welcome'})}<div class="body"><div class="h-big" style="margin-top:6px">С возвращением</div><div class="sub">Войди в аккаунт ВРЫВ</div>
${FIELD('Email','max.kovalev@gmail.com',{icon:'mail',st:'err'})}
${FIELD('Пароль','Пароль',{icon:'lock',st:'err',ph:true,right:ic('eyeOff'),hint:'Неверный Email или пароль. Осталось 2 попытки',hst:'err'})}
<div style="text-align:right;margin-top:14px;font-size:14px" class="link" data-go="rec1">Забыли пароль?</div>
<button class="btn dis" style="margin-top:22px">Войти</button>
<div class="or">или</div><button class="btn sec" data-go="gpick">${GOOGLE}Продолжить с Google</button></div>${HI}`),{state:true});

add('loginLoad','auth','02 · Вход — загрузка','Состояние загрузки: поля заблокированы, в кнопке спиннер, повторное нажатие невозможно. При успехе — переход на главную, при таймауте 10 с — сообщение «Нет соединения» с повтором.',
SCR('loginLoad',`${NAV('',{back:'welcome'})}<div class="body"><div class="h-big" style="margin-top:6px">С возвращением</div><div class="sub">Войди в аккаунт ВРЫВ</div>
<div style="opacity:.5">${FIELD('Email','max.kovalev@gmail.com',{icon:'mail'})}${FIELD('Пароль','••••••••••',{icon:'lock',right:ic('eye')})}</div>
<div style="text-align:right;margin-top:14px;font-size:14px;opacity:.4" class="link">Забыли пароль?</div>
<button class="btn pri" style="margin-top:22px;opacity:.85" data-go="homeP"><span class="spin"></span>Входим…</button>
<div class="or">или</div><button class="btn sec" style="opacity:.4">${GOOGLE}Продолжить с Google</button></div>${HI}`),{state:true});

add('reg','auth','03 · Регистрация','Обязательные поля: имя/никнейм, Email, пароль ×2, город (Минск по умолчанию), согласие. Фото и возраст не спрашиваем: фото — после регистрации, возраст — только при покупке на событие 18+. Требования к паролю видны сразу и отмечаются по мере ввода.',
SCR('reg',`${NAV('Создать аккаунт',{back:'welcome'})}<div class="body" style="top:100px">
${FIELD('Имя или никнейм','Максим')}
${FIELD('Email','max.kovalev@gmail.com',{st:'ok',right:icf('check','#C7FF28').replace('class="ico"','class="ico" style="width:18px;height:18px;fill:none;stroke:#C7FF28;stroke-width:2.6"')})}
${FIELD('Пароль','••••••••',{st:'focus',right:ic('eyeOff')})}
<div style="display:flex;gap:5px;margin:10px 4px 0">${[1,1,1,0].map(o=>`<i style="flex:1;height:4px;border-radius:2px;background:${o?'var(--lime)':'#2A2A33'};display:block"></i>`).join('')}</div>
<div style="display:flex;flex-wrap:wrap;gap:4px 14px;margin:8px 4px 0;font-size:12.5px;font-weight:550"><span style="color:var(--lime)">✓ 8+ символов</span><span style="color:var(--lime)">✓ буква и цифра</span><span class="muted">○ не совпадает с Email</span></div>
${FIELD('Повтори пароль','Ещё раз',{ph:true})}
<div class="fl"><label>Город</label><div class="inp">${ic('pin')}<span class="sp">Минск</span>${ic('chevD')}</div></div>
<div style="display:flex;gap:12px;align-items:flex-start;margin-top:16px"><div class="cbx on">${ic('check')}</div><div style="font-size:13.5px;color:var(--text2);line-height:1.4;font-weight:500">Принимаю <span class="link">Пользовательское соглашение</span> и ознакомлен с <span class="link">Политикой конфиденциальности</span></div></div>
</div><div class="cta"><button class="btn pri" data-go="code">Продолжить</button></div>${HI}`),{states:['regErr']});

add('regErr','auth','03 · Регистрация — ошибки','Ошибки показываются после ухода с поля, а не во время ввода. Занятый Email предлагает войти или восстановить пароль, чтобы не создавать дубль. Кнопка активируется, когда всё заполнено верно.',
SCR('regErr',`${NAV('Создать аккаунт',{back:'welcome'})}<div class="body" style="top:100px">
${FIELD('Имя или никнейм','Максим')}
${FIELD('Email','max.kovalev@gmail.com',{st:'err',hint:'Этот Email уже зарегистрирован. <span class="link" style="margin-left:2px">Войти</span>',hst:'err'})}
${FIELD('Пароль','••••••',{st:'err',right:ic('eyeOff'),hint:'Минимум 8 символов, буква и цифра',hst:'err'})}
${FIELD('Повтори пароль','•••••••',{st:'err',hint:'Пароли не совпадают',hst:'err'})}
<div class="fl"><label>Город</label><div class="inp">${ic('pin')}<span class="sp">Минск</span>${ic('chevD')}</div></div>
<div style="display:flex;gap:12px;align-items:flex-start;margin-top:16px"><div class="cbx" style="box-shadow:inset 0 0 0 1.8px var(--pink)"></div><div style="font-size:13.5px;color:var(--text2);line-height:1.4;font-weight:500">Принимаю <span class="link">Пользовательское соглашение</span> и ознакомлен с <span class="link">Политикой конфиденциальности</span></div></div>
</div><div class="cta"><button class="btn dis">Продолжить</button></div>${HI}`),{state:true});

add('code','auth','04 · Подтверждение Email','6-значный код, срок действия 10 минут, 5 попыток ввода. Автоподстановка кода из письма (oneTimeCode). Повторная отправка доступна через 60 с, далее интервал растёт. Ввод последней цифры сразу отправляет код.',
SCR('code',`${NAV('',{back:'reg'})}<div class="body"><div style="width:60px;height:60px;border-radius:20px;background:var(--card);display:flex;align-items:center;justify-content:center;margin-top:8px;color:var(--pink)">${ic('mail','style="width:28px;height:28px"')}</div>
<div class="h-big" style="margin-top:22px">Проверь почту</div><div class="sub">Отправили код на <b style="color:var(--text)">max.kovalev@gmail.com</b>. <span class="link">Изменить</span></div>
<div class="code" data-go="codeOk"><div>4</div><div>8</div><div>1</div><div class="f"><span class="caret" style="height:28px"></span></div><div></div><div></div></div>
<div class="t-cap" style="margin-top:22px;text-align:center">Отправить снова через <b style="color:var(--text)">0:42</b></div>
<div class="t-cap" style="margin-top:8px;text-align:center;font-size:12px">Код действует 10 минут. Не пришёл — проверь «Спам».</div>
</div><div class="cta"><button class="btn dis">Подтвердить</button></div>${HI}`),{states:['codeErr','codeOk']});

add('codeErr','auth','04 · Код — ошибка','Неверный код: ячейки розовые, лёгкая вибрация, значения очищаются. Код истёк → кнопка «Отправить новый код» активна сразу.',
SCR('codeErr',`${NAV('',{back:'reg'})}<div class="body"><div style="width:60px;height:60px;border-radius:20px;background:var(--card);display:flex;align-items:center;justify-content:center;margin-top:8px;color:var(--pink)">${ic('mail','style="width:28px;height:28px"')}</div>
<div class="h-big" style="margin-top:22px">Проверь почту</div><div class="sub">Отправили код на <b style="color:var(--text)">max.kovalev@gmail.com</b></div>
<div class="code err"><div>4</div><div>8</div><div>1</div><div>9</div><div>0</div><div>2</div></div>
<div class="hint err" style="justify-content:center;margin-top:14px">${ic('alert')}Неверный код. Осталось 3 попытки</div>
<div style="margin-top:20px;text-align:center;font-size:15px" class="link">Отправить новый код</div>
</div>${HI}`),{state:true});

add('codeOk','auth','04 · Код — успех','Код принят: ячейки лаймовые, через 0,6 с создаётся сессия и открывается персонализация. Экран успеха не требует нажатий.',
SCR('codeOk',`${NAV('',{})}<div class="body"><div style="width:60px;height:60px;border-radius:20px;background:var(--lime);display:flex;align-items:center;justify-content:center;margin-top:8px;color:#0B0B0F">${ic('check','style="width:30px;height:30px;stroke-width:2.6"')}</div>
<div class="h-big" style="margin-top:22px">Email подтверждён</div><div class="sub">Осталось 30 секунд — настроим афишу под тебя.</div>
<div class="code ok"><div>4</div><div>8</div><div>1</div><div>9</div><div>0</div><div>7</div></div>
</div><div class="cta"><button class="btn pri" data-go="p1">Настроить рекомендации</button></div>${HI}`),{state:true});

add('rec1','auth','05 · Восстановление · Email','Шаг 1 из 3. Ответ одинаковый, есть Email в базе или нет, — чтобы нельзя было перебирать адреса. Ограничение: 3 запроса в час на адрес и устройство.',
SCR('rec1',`${PROG(1,3,'login')}<div class="body"><div class="h-big" style="margin-top:6px">Восстановить<br>пароль</div><div class="sub">Пришлём код на почту, указанную при регистрации.</div>
${FIELD('Email','max.kovalev@gmail.com',{icon:'mail',st:'focus'})}
</div><div class="cta"><button class="btn pri" data-go="rec2">Отправить код</button></div>${HI}`));

add('rec2','auth','05 · Восстановление · код','Шаг 2 из 3. Тот же компонент ввода кода, что при регистрации. Также в письме есть одноразовая ссылка (Universal Link), которая открывает шаг 3 сразу. Срок действия — 15 минут.',
SCR('rec2',`${PROG(2,3,'rec1')}<div class="body"><div class="h-big" style="margin-top:6px">Введи код</div><div class="sub">Код из письма на <b style="color:var(--text)">max.kovalev@gmail.com</b>. Можно просто нажать на ссылку в письме.</div>
<div class="code" data-go="rec3"><div>2</div><div>7</div><div>5</div><div>0</div><div>1</div><div class="f"><span class="caret" style="height:28px"></span></div></div>
<div class="t-cap" style="margin-top:22px;text-align:center">Отправить снова через <b style="color:var(--text)">0:58</b></div>
</div>${HI}`));

add('rec3','auth','05 · Восстановление · новый пароль','Шаг 3 из 3. Те же требования, что при регистрации, плюс «не совпадает со старым». После смены все остальные сессии завершаются, на почту приходит уведомление.',
SCR('rec3',`${PROG(3,3,'rec2')}<div class="body"><div class="h-big" style="margin-top:6px">Новый пароль</div><div class="sub">После смены выйдем на других устройствах.</div>
${FIELD('Новый пароль','••••••••••',{right:ic('eye'),st:'ok'})}
<div style="display:flex;gap:5px;margin:10px 4px 0">${[1,1,1,1].map(()=>`<i style="flex:1;height:4px;border-radius:2px;background:var(--lime);display:block"></i>`).join('')}</div>
<div style="display:flex;flex-wrap:wrap;gap:4px 14px;margin:8px 4px 0;font-size:12.5px;font-weight:550;color:var(--lime)"><span>✓ 8+ символов</span><span>✓ буква и цифра</span><span>✓ не как старый</span></div>
${FIELD('Повтори пароль','••••••••••',{st:'ok',hint:'Пароли совпадают',hst:'ok'})}
</div><div class="cta"><button class="btn pri" data-go="rec4">Сохранить пароль</button></div>${HI}`));

add('rec4','auth','05 · Восстановление · готово','Подтверждение без лишних шагов: сессия уже создана, кнопка ведёт на главную.',
SCR('rec4',`<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;padding:0 32px;text-align:center"><div style="width:84px;height:84px;border-radius:28px;background:var(--lime);display:flex;align-items:center;justify-content:center;color:#0B0B0F">${ic('check','style="width:40px;height:40px;stroke-width:2.6"')}</div><div class="h-big" style="margin-top:28px">Пароль обновлён</div><div class="sub">Ты вошёл в аккаунт. На других устройствах потребуется новый пароль.</div></div>
<div class="cta"><button class="btn pri" data-go="homeP">Перейти в афишу</button></div>${HI}`));

add('guest','auth','Гостевой режим · окно входа','Гость видит публичную афишу и карту. Действия «сохранить», «Иду», свайп, чат вызывают это окно. После входа выбранное действие выполняется автоматически, без повторного нажатия.',
SCR('guest',`<img src="out/raw/screen_01.png" style="position:absolute;inset:0;width:100%;height:100%"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div style="display:flex;justify-content:center;margin:6px 0 4px;color:var(--pink)">${icf('heart','#FF1D8E').replace('class="ico"','class="ico" style="width:34px;height:34px;fill:#FF1D8E;stroke:#FF1D8E"')}</div>
<div class="t-title" style="text-align:center;margin-top:10px">Сохраним это событие?</div><div class="sub" style="text-align:center">Войди, чтобы сохранять события, свайпать, собирать компанию и вести планы.</div>
<button class="btn white" style="margin-top:20px" data-go="gpick">${GOOGLE}Продолжить с Google</button>
<button class="btn sec" style="margin-top:10px" data-go="login">${ic('mail','style="width:20px;height:20px"')}Войти по Email</button>
<div style="text-align:center;margin-top:16px;font-size:15px;color:var(--text2);font-weight:550" data-go="welcome">Не сейчас</div></div>`));

/* ===== ПЕРСОНАЛИЗАЦИЯ ===== */
const INT=['Техно','Хаус','Концерты','Рок','Хип-хоп','Стендап','Бары','Клубы','Выставки','Кино','Квизы','Лекции','Искусство','Спорт','Кофейни','Новые знакомства','Андеграунд','Бесплатные мероприятия'];
const ON=['Техно','Хаус','Стендап','Выставки','Андеграунд'];
add('p1','pers','06 · Интересы · 1 из 3','Чипы с множественным выбором. Нажатие — розовый цвет и лёгкая вибрация. «Продолжить» активна от 3 интересов, на кнопке счётчик. «Пропустить» включает общие рекомендации (популярное в Минске), шаг можно пройти позже в профиле. Выбор сразу сохраняется в профиль.',
SCR('p1',`${PROG(1,3,'',"p2")}<div class="body"><div class="h-big" style="margin-top:6px">Что тебе по вайбу?</div><div class="sub">Выбери то, что нравится. Мы найдём события под тебя.</div>
<div style="display:flex;flex-wrap:wrap;gap:9px;margin-top:24px">${INT.map(t=>`<div class="chip big ${ON.includes(t)?'sel':''}">${ON.includes(t)?ic('check','style="width:16px;height:16px;stroke-width:2.6"'):''}${t}</div>`).join('')}</div>
</div><div class="cta"><div class="t-cap" style="text-align:center;margin-bottom:12px">Минимум 3 — так рекомендации будут точнее</div><button class="btn pri" data-go="p2">Продолжить · 5</button></div>${HI}`),{states:['p1e']});

add('p1e','pers','06 · Интересы — пусто','Начальное состояние: ничего не выбрано, кнопка неактивна, подсказка объясняет минимум. Пропуск всегда доступен.',
SCR('p1e',`${PROG(1,3,'',"p2")}<div class="body"><div class="h-big" style="margin-top:6px">Что тебе по вайбу?</div><div class="sub">Выбери то, что нравится. Мы найдём события под тебя.</div>
<div style="display:flex;flex-wrap:wrap;gap:9px;margin-top:24px">${INT.map((t,i)=>`<div class="chip big ${i<1?'sel':''}">${i<1?ic('check','style="width:16px;height:16px;stroke-width:2.6"'):''}${t}</div>`).join('')}</div>
</div><div class="cta"><div class="t-cap" style="text-align:center;margin-bottom:12px">Выбери ещё 2</div><button class="btn dis">Продолжить · 1</button></div>${HI}`),{state:true});

const BUD=[['Бесплатно','Вход свободный'],['До 20 BYN','Бары, лекции, квизы'],['До 50 BYN','Большинство вечеринок и концертов'],['До 100 BYN','Крупные концерты и шоу'],['Цена неважна','Покажем всё']];
add('p2','pers','07 · Бюджет · 2 из 3','Один вариант (radio). Бюджет — мягкий сигнал: влияет на порядок в ленте, но не скрывает события дороже. Изменить можно в профиле или временно фильтром в афише.',
SCR('p2',`${PROG(2,3,'p1',"p3")}<div class="body"><div class="h-big" style="margin-top:6px">Какой у тебя бюджет?</div><div class="sub">Подберём события, которые подходят тебе.</div>
<div style="margin-top:22px">${BUD.map(([t,s],i)=>`<div class="opt ${i===2?'on':''}"><div style="flex:1"><div>${t}</div><div class="sub2">${s}</div></div><div class="radio ${i===2?'on':''}"></div></div>`).join('')}</div>
<div style="display:flex;gap:10px;align-items:flex-start;margin-top:8px;padding:0 4px"><span style="color:var(--text2)">${ic('alert','style="width:17px;height:17px"')}</span><span class="t-cap" style="line-height:1.4">Дорогие события не исчезнут — просто будут ниже в ленте.</span></div>
</div><div class="cta"><button class="btn pri" data-go="p3">Продолжить</button></div>${HI}`));

const FMT=[['users','Люблю ходить с друзьями',1],['spark','Хочу знакомиться с новыми людьми',1],['user','Предпочитаю ходить один',0],['zap','Люблю спонтанные мероприятия',1],['calendar','Планирую выходные заранее',0],['coffee','Люблю спокойные места',0],['moon','Предпочитаю активную ночную жизнь',1]];
add('p3','pers','08 · Формат отдыха · 3 из 3','Множественный выбор. «Новые знакомства» поднимает в ленте события с поиском компании, «спонтанные» — раздел «Прямо сейчас» и пуши за 2–3 часа, «заранее» — подборку на выходные в четверг.',
SCR('p3',`${PROG(3,3,'p2',"p4")}<div class="body"><div class="h-big" style="margin-top:6px">Как любишь проводить время?</div><div class="sub">Можно выбрать несколько.</div>
<div style="margin-top:20px">${FMT.map(([i,t,o])=>`<div class="opt ${o?'on':''}" style="height:58px;font-size:15px"><div class="oico" style="width:36px;height:36px;${o?'color:var(--pink)':''}">${ic(i)}</div><div style="flex:1">${t}</div><div class="cbx ${o?'on':''}">${o?ic('check'):''}</div></div>`).join('')}</div>
</div><div class="cta"><button class="btn pri" data-go="p4">Готово</button></div>${HI}`));

add('p4','pers','09 · Всё готово','Финал. Показываем, что уже нашлось (количество — реальный результат запроса к ленте). «Начать врываться» открывает персональную афишу; флаг onboarding_completed сохраняется в профиле, поэтому экраны больше не показываются.',
SCR('p4',`<div style="position:absolute;top:96px;left:0;right:0;display:flex;justify-content:center"><img src="ph/logo.png" style="width:120px"></div>
<div style="position:absolute;top:250px;left:24px;right:24px;text-align:center"><div class="t-display" style="font-size:36px">Всё готово.<br>Минск ждёт тебя!</div><div class="sub" style="font-size:16px">Уже нашли <b style="color:var(--text)">47 событий</b> на этой неделе</div></div>
<div style="position:absolute;top:420px;left:0;right:0;display:flex;justify-content:center"><div style="position:relative;width:300px;height:220px">
<div class="ph" style="position:absolute;left:0;top:30px;width:130px;height:170px;border-radius:22px;transform:rotate(-8deg)"><img src="${I('jazz')}"></div>
<div class="ph" style="position:absolute;right:0;top:30px;width:130px;height:170px;border-radius:22px;transform:rotate(8deg)"><img src="${I('standup')}"></div>
<div class="ph" style="position:absolute;left:78px;top:0;width:144px;height:192px;border-radius:24px;box-shadow:0 16px 40px rgba(0,0,0,.6)"><img src="${I('techno')}"><div class="in" style="left:10px;bottom:10px"><span class="tag lime">96%</span></div></div></div></div>
<div style="position:absolute;top:668px;left:0;right:0;display:flex;justify-content:center;gap:6px;flex-wrap:wrap;padding:0 30px">${['Техно','Хаус','Стендап','До 50 BYN','С друзьями','Спонтанно'].map(t=>`<span class="tag">${t}</span>`).join('')}</div>
<div class="cta"><button class="btn pri" data-go="homeP">Начать врываться</button></div>${HI}`));
