/* ===== MVP · КОМПАНИЯ (поиск, участники, создание, страница, чат) ===== */
const USR=(a,n,s,rt='')=>`<div class="li noi" style="min-height:64px"${rt}>${AV(a,46)}<div><div style="font-size:15.5px">${n}</div><div class="t-cap" style="margin-top:2px;font-size:12.5px">${s}</div></div></div>`;

add('compSearch','user','25 · Компания · поиск','Поиск привязан к карточке мероприятия (контекст сверху, можно сменить). Открытая группа — вступление сразу, закрытая — заявка: создатель видит её на странице компании и решает. Проценты совместимости не показываем — только общие интересы.',
`<div class="screen long" id="compSearch">${STATUS}
<div class="hdr" style="position:absolute;top:54px;left:0;right:0;height:52px;display:flex;align-items:center;justify-content:space-between;padding:0 16px"><div class="t-title">Компания</div><div class="ibtn" data-go="compNew">${ic('plus')}</div></div>
<div style="padding:116px 16px 0">
<div class="searchbar" style="height:46px;border-radius:23px;background:var(--card);display:flex;align-items:center;gap:10px;padding:0 16px;color:var(--text2)">${ic('search','style="width:19px;height:19px"')}Найти группу или человека</div>
<div style="background:var(--card);border-radius:20px;padding:12px;margin-top:14px;display:flex;gap:12px;align-items:center" data-go="event"><div class="ph" style="width:52px;height:52px;border-radius:13px;flex:none"><img src="${I('techno')}"></div><div style="flex:1"><div class="t-cap" style="font-size:11.5px;letter-spacing:.06em;text-transform:uppercase">Ищем компанию на</div><div class="t-h3" style="font-size:15px;margin-top:2px">Techno Room: Ночь 3</div><div class="t-cap" style="margin-top:2px">Сегодня · 23:00 · Re:Public</div></div>${ic('swap','style="color:var(--text2)"')}</div>
<div class="sec-h" style="margin:22px 0 10px"><span class="t-h2" style="font-size:20px">Группы на это мероприятие</span><a data-go="compNew">Создать</a></div>
<div style="background:var(--card);border-radius:22px;padding:14px">
<div style="display:flex;gap:12px;align-items:center"><div class="stack oncard" style="flex:none">${AV('av1',26)}${AV('av5',26)}${AV('av4',26)}</div><div style="flex:1;min-width:0"><div class="t-h3" style="font-size:15.5px">Идём на Techno Room</div><div class="t-cap" style="margin-top:3px">Сбор 22:30 · у входа Re:Public · 3 из 5 мест</div></div></div>
<div style="display:flex;gap:7px;flex-wrap:wrap;margin-top:12px">${['Техно','Хаус','23–30 лет'].map(t=>`<span class="tag" style="height:26px;font-size:12px;padding:0 10px">${t}</span>`).join('')}</div>
<div style="display:flex;gap:10px;margin-top:12px"><button class="btn pri sm" style="flex:1;height:40px" data-go="compMember">Присоединиться</button><div class="ibtn" style="width:40px;height:40px;border-radius:20px" data-go="compChat">${ic('chat','style="width:19px;height:19px"')}</div></div></div>
<div style="background:var(--card);border-radius:22px;padding:14px;margin-top:10px">
<div style="display:flex;gap:12px;align-items:center"><div class="stack oncard" style="flex:none">${AV('av3',26)}${AV('av6',26)}</div><div style="flex:1;min-width:0"><div class="t-h3" style="font-size:15.5px">Афтепати после сета</div><div class="t-cap" style="margin-top:3px">Сбор 02:00 · рядом с Re:Public · 2 из 4 мест</div></div>${ic('lock','style="width:18px;height:18px;color:var(--text2)"')}</div>
<div style="display:flex;gap:7px;flex-wrap:wrap;margin-top:12px">${['Стендап','Бары','Спонтанно'].map(t=>`<span class="tag" style="height:26px;font-size:12px;padding:0 10px">${t}</span>`).join('')}</div>
<button class="btn sec sm" style="width:100%;height:40px;margin-top:12px" data-go="compSent">Запросить вход</button></div>
<div class="sec-h" style="margin:24px 0 10px"><span class="t-h2" style="font-size:20px">Тоже идут одни</span><a data-go="member">Все</a></div>
${[['av1','Алина, 24','@alina.tech','Техно · Хаус · Ночная жизнь'],['av4','Дима, 25','@dima.vinyl','Техно · Андеграунд'],['av3','Катя, 22','@katya.ink','Выставки · Квизы'],['av6','Артём, 30','@artem_jazz','Джаз · Бары']].map(([a,n,u,s])=>USR(a,`${n} <span class="muted" style="font-weight:400;font-size:13px">${u}</span>`,s,' data-go="member"')).join('')}
<div style="display:flex;gap:8px;align-items:center;margin:14px 2px 0" class="t-cap">${ic('lock','style="width:15px;height:15px"')}Показываем только тех, кто открыл планы и разрешил контакт</div>
</div>${UTAB(3)}</div>`,{long:true});

add('member','user','26 · Карточка участника','Аватар, имя, город и краткая информация. Вместо процента совместимости — общие интересы чипами. Действия: «Пойти вместе» отправляет заявку на мероприятие, «Написать» открывает личный чат. Троеточие — жалоба и блокировка.',
SCR('member',`${NAV('',{back:'compSearch'})}<div class="body" style="text-align:center">
<div style="display:flex;justify-content:flex-end;margin-top:2px"><div class="ibtn" style="background:transparent" data-go="block">${ic('dots','style="width:22px;height:22px"')}</div></div>
${AV('av1',96,'margin:0 auto;box-shadow:0 0 0 2px var(--bg),0 0 0 4px var(--pink)')}
<div class="t-title" style="margin-top:14px">Алина, 24</div><div class="t-cap" style="margin-top:4px">@alina.tech · ${ic('pin','style="width:13px;height:13px;display:inline;vertical-align:-2px"')} Минск</div>
<div style="font-size:15px;line-height:1.45;margin-top:14px;color:#D9D9DE">Техно по пятницам, выставки по воскресеньям. Иду на Techno Room одна — ищу компанию.</div>
<div class="grp-t" style="text-align:left">Общие интересы</div>
<div style="display:flex;gap:7px;flex-wrap:wrap;justify-content:center">${['Техно','Хаус','Андеграунд','Ночная жизнь'].map(t=>`<span class="chip sel" style="height:32px;font-size:13.5px">${t}</span>`).join('')}</div>
<div style="display:flex;gap:8px;margin:18px 0 0">${[['4','События вместе'],['86','Друзей']].map(([n,l])=>`<div class="stat" style="flex:1;background:var(--card);border-radius:18px;padding:12px"><b style="font-size:20px;font-weight:720;display:block">${n}</b><span class="t-cap">${l}</span></div>`).join('')}</div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="compSent">Пойти вместе</button><button class="btn sec" data-go="lchat">${ic('chat','style="width:18px;height:18px"')}Написать</button></div>${HI}`));

add('compSent','user','27 · Заявка отправлена','Состояние после заявки в закрытую группу или «Пойти вместе»: заявка со статусом «Ждёт ответа», чат компании закрыт, пока создатель не принял. Отменить можно в один тап.',
SCR('compSent',`${NAV('',{back:'member'})}<div class="body" style="text-align:center;padding-top:40px">
<div style="width:72px;height:72px;border-radius:36px;background:var(--card);display:flex;align-items:center;justify-content:center;margin:0 auto">${ic('clock','style="width:34px;height:34px;color:var(--lime)"')}</div>
<div class="h-big" style="margin-top:18px">Заявка отправлена</div>
<div class="sub" style="max-width:290px;margin:8px auto 0">Алина получит уведомление. Пока заявка не принята, чат компании закрыт.</div>
<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:20px;text-align:left;display:flex;gap:12px;align-items:center">${AV('av1',44)}<div><div style="font-size:15px">Заявка · Ждёт ответа</div><div class="t-cap" style="margin-top:2px">Techno Room: Ночь 3 · сегодня</div></div><span class="status st-wait" style="height:24px"><i></i>Ожидание</span></div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="back">Понятно</button><button class="btn ghost" style="color:var(--pink)" data-go="back">Отменить заявку</button></div>${HI}`));

add('compNew','user','28 · Создание компании','Название, мероприятие (подставляется из карточки), время и место встречи, лимит участников, открытый или закрытый доступ. Открытая группа видна в поиске и принимает сразу; закрытая — только по заявке.',
SCR('compNew',`${NAV('Новая компания',{back:'compSearch'})}<div class="body">
${FIELD('Название','Идём на Techno Room',{st:'focus'})}
<div class="grp-t">Мероприятие</div>
<div style="background:var(--card);border-radius:18px;padding:10px;display:flex;gap:12px;align-items:center" data-go="event"><div class="ph" style="width:52px;height:52px;border-radius:13px;flex:none"><img src="${I('techno')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px">Techno Room: Ночь 3</div><div class="t-cap" style="margin-top:2px">Сегодня · 23:00 · Re:Public</div></div>${ic('chevR','style="color:var(--text2)"')}</div>
${FIELD('Время встречи','Сегодня · 22:30',{icon:'clock'})}
${FIELD('Место сбора','У входа Re:Public',{icon:'pin'})}
<div class="grp-t">Количество участников</div>
<div style="display:flex;gap:8px">${['2','4','6','8'].map((t,i)=>`<div class="chip ${i===1?'on':''}" style="flex:1;height:40px;font-size:15px">${t}</div>`).join('')}</div>
<div class="grp" style="margin-top:16px">${LI('lock','Открытая компания','Вступление без заявки',{tg:true})}${LI('users','Показывать в поиске','В списке компании на это мероприятие',{tg:true})}</div>
<div class="t-cap" style="padding:0 4px;line-height:1.4">Закрытая компания принимает только по заявке — создатель видит и решает каждую.</div>
</div><div class="cta"><button class="btn pri" data-go="compPage">Создать компанию</button></div>${HI}`));

add('compPage','user','29 · Страница компании · создатель','Вид создателя: участники, место и время встречи, карточка мероприятия, приглашение друзей и управление заявками — принять или отклонить. Открытая группа принимает сразу; закрытая — через этот список.',
SCR('compPage',`${NAV('',{back:'compSearch'})}<div class="body" style="padding-top:8px">
<div style="display:flex;gap:10px;align-items:center"><div class="stack oncard" style="flex:none">${AV('av2',26)}${AV('av1',26)}${AV('av4',26)}</div><div style="flex:1"><div class="t-title" style="font-size:22px">Идём на Techno Room</div><div class="t-cap" style="margin-top:3px">Открытая · 3 из 5 мест</div></div><div class="ibtn" style="background:transparent" data-go="report" data-rctx="group">${ic('dots','style="width:22px;height:22px"')}</div></div>
<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:14px;display:flex;gap:12px;align-items:center" data-go="event"><div class="ph" style="width:60px;height:60px;border-radius:15px;flex:none"><img src="${I('techno')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px">Techno Room: Ночь 3</div><div class="t-cap" style="margin-top:3px">Сегодня · 23:00 · Re:Public</div></div>${ic('chevR','style="color:var(--text2)"')}</div>
<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:10px"><div style="display:flex;gap:12px"><div class="lico">${ic('clock')}</div><div><div style="font-size:15px">Встреча в 22:30</div><div class="t-cap" style="margin-top:2px">У входа Re:Public · не опаздывать</div></div></div></div>
<div class="grp-t" style="margin-top:20px">Участники · 3</div>
<div class="grp">${USR('av2','Максим · вы','Создатель компании')}${USR('av1','Алина','@alina.tech')}${USR('av4','Дима','@dima.vinyl')}</div>
<div class="grp-t" style="margin-top:18px">Заявки · 1</div>
<div class="grp">${[['av3','Катя','@katya.ink · 2 общих интереса']].map(([a,n,s])=>`<div class="li noi" style="min-height:62px">${AV(a,44)}<div><div style="font-size:15px">${n}</div><div class="t-cap" style="font-size:12.5px">${s}</div></div><span class="v" style="gap:6px"><button class="btn sec sm" style="height:30px;padding:0 12px;font-size:13px">Отклонить</button><button class="btn pri sm" style="height:30px;padding:0 12px;font-size:13px">Принять</button></span></div>`).join('')}</div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="compChat">${ic('chat','style="width:18px;height:18px"')}Чат компании</button><button class="btn sec" data-go="friendsPick">Пригласить друзей</button></div>${HI}`,'long'));

add('compMember','user','30 · Страница компании · участник','Та же страница глазами участника: без управления заявками. Выход из компании — в один тап с подтверждением; чат и планы при этом остаются у пользователя.',
SCR('compMember',`${NAV('',{back:'compSearch'})}<div class="body" style="padding-top:8px">
<div style="display:flex;gap:10px;align-items:center"><div class="stack oncard" style="flex:none">${AV('av2',26)}${AV('av1',26)}${AV('av4',26)}${AV('av5',26)}</div><div style="flex:1"><div class="t-title" style="font-size:22px">Идём на Techno Room</div><div class="t-cap" style="margin-top:3px">Открытая · 4 из 5 мест</div></div><div class="ibtn" style="background:transparent" data-go="report" data-rctx="group">${ic('dots','style="width:22px;height:22px"')}</div></div>
<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:14px;display:flex;gap:12px;align-items:center" data-go="event"><div class="ph" style="width:60px;height:60px;border-radius:15px;flex:none"><img src="${I('techno')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px">Techno Room: Ночь 3</div><div class="t-cap" style="margin-top:3px">Сегодня · 23:00 · Re:Public</div><div class="t-cap" style="margin-top:2px;color:var(--lime)">Встреча в 22:30 · у входа</div></div>${ic('chevR','style="color:var(--text2)"')}</div>
<div class="grp-t" style="margin-top:20px">Участники · 4</div>
<div class="grp">${USR('av2','Максим','Создатель компании')}${USR('av1','Алина','@alina.tech')}${USR('av4','Дима','@dima.vinyl')}${USR('av5','Вика','@vika.ink')}</div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="compChat">${ic('chat','style="width:18px;height:18px"')}Чат компании</button><button class="btn sec" data-go="friendsPick">Пригласить друзей</button><button class="btn ghost" style="color:var(--pink)" data-go="compSearch">Выйти из компании</button></div>${HI}`,'long'));

add('compChat','user','31 · Чат компании','Общение участников: сообщения, быстрые ответы и отправка своих. Состав группы — шторкой сверху. Выход из компании — с подтверждением, из чата или со страницы компании.',
SCR('compChat',`<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between;align-items:center"><div style="display:flex;align-items:center;gap:10px" data-go="back"><div class="gh" style="margin-left:-12px">${ic('chevL','style="width:24px;height:24px"')}</div><div class="stack oncard" style="flex:none">${AV('av1',22)}${AV('av4',22)}</div><div><div class="t-title" style="font-size:19px">Идём на Techno Room</div><div class="t-cap" style="font-size:12px;margin-top:2px">4 участника · сегодня</div></div></div><div style="display:flex;gap:8px"><div class="ibtn" data-go="compMembers">${ic('users','style="width:19px;height:19px"')}</div><div class="ibtn" data-go="report" data-rctx="group">${ic('dots','style="width:20px;height:20px"')}</div></div></div>
<div class="body" style="padding-top:110px">
<div class="t-cap" style="text-align:center">Сегодня</div>
<div style="display:flex;gap:10px;margin-top:14px">${AV('av4',30)}<div class="bubble in">Встречаемся в 22:30 у входа, ок?</div></div>
<div style="display:flex;gap:10px;margin-top:8px;justify-content:flex-end"><div class="bubble out">Ок! Займу очередь в гардероб</div></div>
<div style="display:flex;gap:10px;margin-top:8px">${AV('av1',30)}<div class="bubble in">Я опоздаю минут на 15, начните без меня</div></div>
<div class="t-cap" style="text-align:center;margin-top:12px;color:var(--lime)">Катя присоединилась к компании</div>
</div>
<div style="position:absolute;left:0;right:0;bottom:0;padding:10px 16px 34px;background:var(--bg);border-top:1px solid var(--line)"><div style="display:flex;gap:8px;margin-bottom:10px;overflow:hidden">${['Отлично!','Кто за баром после?','Беру подругу'].map(t=>`<span class="chip sm" style="height:30px">${t}</span>`).join('')}</div><div style="display:flex;gap:10px;align-items:center"><div class="inp" style="flex:1;height:44px;border-radius:22px"><span class="sp ph-t">Сообщение</span></div><div class="ibtn" style="background:var(--pink);width:44px;height:44px;border-radius:22px">${ic('send','style="width:19px;height:19px"')}</div></div></div>${HI}`));

add('compMembers','user','32 · Состав компании','Шторка состава поверх чата: участники с ролями, создатель помечен. Отсюда — выход из компании с подтверждением и жалоба на участника.',
SCR('compMembers',`<img src="out/raw2/compChat.png" style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;object-position:top"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div style="display:flex;justify-content:space-between;align-items:center"><div class="t-h3">Состав · 4</div><div class="ibtn" style="width:36px;height:36px;background:transparent" data-go="back">${ic('x','style="width:20px;height:20px"')}</div></div>
<div class="grp" style="margin-top:10px">${USR('av2','Максим','Создатель компании')}${USR('av1','Алина','@alina.tech')}${USR('av4','Дима','@dima.vinyl')}${USR('av5','Вика','@vika.ink')}</div>
<div class="t-cap" style="margin-top:12px;padding:0 4px;line-height:1.4">При выходе ты покинешь чат и компанию. План на мероприятие останется.</div>
<div style="display:flex;gap:10px;margin-top:12px"><button class="btn ghost" style="flex:1;color:var(--pink)" data-go="compSearch">Выйти из компании</button><button class="btn sec" style="flex:1" data-go="report" data-rctx="group">${ic('flag','style="width:17px;height:17px"')}Пожаловаться</button></div></div>`));

/* ===== MVP · ЛИЧНЫЕ СООБЩЕНИЯ ===== */
add('msgs','user','33 · Личные сообщения','Список диалогов пользователей: непрочитанные, последнее сообщение, время. Вход — из профиля, друзей и карточки участника. Массовые рассылки недоступны.',
SCR('msgs',`${NAV('Сообщения',{back:'prof'})}<div class="body">
<div class="searchbar" style="height:46px;border-radius:23px;background:var(--card);display:flex;align-items:center;gap:10px;padding:0 16px;color:var(--text2)">${ic('search','style="width:19px;height:19px"')}Поиск по диалогам</div>
<div class="grp-t" style="margin-top:16px">Непрочитанные · 2</div>
<div class="grp">${[['av1','Алина','Идёшь завтра на Techno?','19:42',2],['av4','Дима','Передал тебе билет, проверь','18:05',1]].map(([a,n,m,t,u])=>`<div class="li noi" style="min-height:66px" data-go="lchat">${AV(a,48)}<div style="flex:1;min-width:0"><div style="display:flex;justify-content:space-between"><span style="font-size:15.5px">${n}</span><span class="t-cap" style="font-size:11.5px">${t}</span></div><div class="t-cap" style="margin-top:2px;${u?'color:var(--text)':''}">${m}</div></div>${u?`<span class="dot" style="align-self:center"></span>`:''}</div>`).join('')}</div>
<div class="grp-t" style="margin-top:16px">Ранее</div>
<div class="grp">${[['av3','Катя','Скинула фотки с квиза','вт'],['av6','Артём','Бар после стендапа — ты в деле?','пн']].map(([a,n,m,t])=>`<div class="li noi" style="min-height:66px" data-go="lchat">${AV(a,48)}<div style="flex:1;min-width:0"><div style="display:flex;justify-content:space-between"><span style="font-size:15.5px">${n}</span><span class="t-cap" style="font-size:11.5px">${t}</span></div><div class="t-cap" style="margin-top:2px">${m}</div></div></div>`).join('')}</div>
</div>${HI}`));

add('lchat','user','34 · Личный чат','Переписка один на один: сообщения, быстрые ответы, отправка. Троеточие в шапке — меню с блокировкой и жалобой. Диалог привязан к пользователю, не к заведению.',
SCR('lchat',`<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between;align-items:center"><div style="display:flex;align-items:center;gap:10px" data-go="msgs"><div class="gh" style="margin-left:-12px">${ic('chevL','style="width:24px;height:24px"')}</div>${AV('av1',38)}<div><div class="t-title" style="font-size:19px">Алина</div><div class="t-cap" style="font-size:12px;margin-top:2px">@alina.tech · в сети</div></div></div><div class="ibtn" data-go="block">${ic('dots','style="width:20px;height:20px"')}</div></div>
<div class="body" style="padding-top:110px">
<div class="t-cap" style="text-align:center">Сегодня</div>
<div style="display:flex;gap:10px;margin-top:14px"><div class="bubble in">Привет! Ты же был на прошлом Techno Room?</div></div>
<div style="display:flex;gap:10px;margin-top:8px;justify-content:flex-end"><div class="bubble out">Да, было мощно. Идёшь завтра?</div></div>
<div style="display:flex;gap:10px;margin-top:8px"><div class="bubble in">Иду! Одна не хочу, пойдём вместе?</div></div>
</div>
<div style="position:absolute;left:0;right:0;bottom:0;padding:10px 16px 34px;background:var(--bg);border-top:1px solid var(--line)"><div style="display:flex;gap:8px;margin-bottom:10px;overflow:hidden">${['Пойдём!','Кто ещё идёт?','Во сколько встречаемся?'].map(t=>`<span class="chip sm" style="height:30px">${t}</span>`).join('')}</div><div style="display:flex;gap:10px;align-items:center"><div class="inp" style="flex:1;height:44px;border-radius:22px"><span class="sp ph-t">Сообщение</span></div><div class="ibtn" style="background:var(--pink);width:44px;height:44px;border-radius:22px">${ic('send','style="width:19px;height:19px"')}</div></div></div>${HI}`));

add('block','user','35 · Действия с пользователем','Меню личного чата: блокировка и жалоба. Блокировка скрывает переписку и профиль друг от друга; жалоба уходит на модерацию — обработка на отдельном админ-сайте, не в приложении.',
SCR('block',`<img src="out/raw2/lchat.png" style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;object-position:top"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div style="display:flex;gap:12px;align-items:center;padding:4px 0 14px">${AV('av1',52)}<div><div style="font-size:16px">Алина</div><div class="t-cap" style="margin-top:2px">@alina.tech</div></div></div>
<div class="grp"><div class="li" data-go="report" data-rctx="user"><div class="lico">${ic('flag')}</div><span>Пожаловаться</span><span class="v">${ic('chevR')}</span></div>${LI('ban','Заблокировать','Не сможет писать',{danger:true,go:'blocked'})}</div>
<div style="display:flex;gap:10px;margin-top:14px"><button class="btn sec" style="flex:1" data-go="back">Отмена</button></div></div>`));

add('blocked','user','36 · После блокировки','Видимое состояние после блокировки: переписка скрыта, отправка сообщений недоступна. Разблокировка возвращает диалог, но не восстанавливает удалённое.',
SCR('blocked',`<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between;align-items:center"><div style="display:flex;align-items:center;gap:10px" data-go="msgs"><div class="gh" style="margin-left:-12px">${ic('chevL','style="width:24px;height:24px"')}</div>${AV('av1',38,'filter:grayscale(1);opacity:.55')}<div><div class="t-title" style="font-size:19px;opacity:.55">Алина</div><div class="t-cap" style="font-size:12px;margin-top:2px">Заблокирована</div></div></div><div class="ibtn" data-go="report" data-rctx="user">${ic('dots','style="width:20px;height:20px"')}</div></div>
<div class="body" style="display:flex;flex-direction:column;align-items:center;justify-content:center;text-align:center;padding-top:150px">
<div style="width:72px;height:72px;border-radius:36px;background:var(--card);display:flex;align-items:center;justify-content:center">${ic('ban','style="width:34px;height:34px;color:var(--pink)"')}</div>
<div class="h-big" style="margin-top:18px;font-size:24px">Алина заблокирована</div>
<div class="sub" style="max-width:290px;margin-top:8px">Она не видит твой профиль и не может писать. Прежние сообщения скрыты.</div>
</div><div class="cta"><button class="btn sec" data-go="lchat">Разблокировать</button></div>${HI}`));

/* ===== MVP · ПРИГЛАШЕНИЯ ДРУЗЕЙ ===== */
add('friendsPick','user','37 · Приглашение друзей','Выбор друзей для компании или мероприятия: поиск, мультивыбор с чекбоксами, счётчик выбранных. Отправка — пуш и сообщение в личный чат; приглашение в компанию не смешивается с заявкой в друзья.',
SCR('friendsPick',`${NAV('Пригласить друзей',{back:'compPage'})}<div class="body">
<div style="background:var(--card);border-radius:18px;padding:12px;display:flex;gap:12px;align-items:center"><div class="ph" style="width:52px;height:52px;border-radius:13px;flex:none"><img src="${I('techno')}"></div><div><div class="t-h3" style="font-size:15px">Techno Room: Ночь 3</div><div class="t-cap" style="margin-top:2px">Сегодня · 23:00 · компания из 3</div></div></div>
<div class="searchbar" style="height:46px;border-radius:23px;background:var(--card);display:flex;align-items:center;gap:10px;padding:0 16px;margin-top:14px;color:var(--text2)">${ic('search','style="width:19px;height:19px"')}Найти друга</div>
<div class="grp-t" style="margin-top:16px">Выбрано · 3</div>
<div class="grp">${[['av1','Алина','@alina.tech · идёт на техно',1],['av4','Дима','@dima.vinyl · был на прошлом',1],['av5','Вика','@vika.ink',1],['av3','Катя','@katya.ink · планы скрыты',0],['av6','Артём','@artem_jazz',0]].map(([a,n,s,on])=>`<div class="li noi" style="min-height:62px">${AV(a,44)}<div><div style="font-size:15px">${n}</div><div class="t-cap" style="font-size:12.5px">${s}</div></div><div class="chk ${on?'on':''}"></div></div>`).join('')}</div>
</div><div class="cta"><button class="btn pri" data-go="invSent">Отправить приглашение · 3</button></div>${HI}`));

add('invSent','user','38 · Приглашения отправлены','Состояние после отправки: трое друзей получат пуш со ссылкой на компанию. Ответ — «иду» или «не смогу» — придёт в уведомления и в чат компании.',
SCR('invSent',`${NAV('',{back:'compPage'})}<div class="body" style="text-align:center;padding-top:36px">
<div style="width:72px;height:72px;border-radius:36px;background:var(--card);display:flex;align-items:center;justify-content:center;margin:0 auto">${ic('send','style="width:32px;height:32px;color:var(--lime);transform:rotate(-15deg)"')}</div>
<div class="h-big" style="margin-top:18px">Приглашения отправлены</div>
<div class="sub" style="max-width:290px;margin:8px auto 0">Алина, Дима и Вика получат пуш со ссылкой на компанию и мероприятие.</div>
<div style="display:flex;gap:8px;justify-content:center;margin-top:18px">${['av1','av4','av5'].map(a=>AV(a,44)).join('')}</div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="compPage">К компании</button><button class="btn ghost" data-go="back">Позже</button></div>${HI}`));

add('invIn','user','39 · Входящее приглашение','Друг зовёт на мероприятие или в компанию: карточка события, кто уже идёт, принять или отклонить. Принятие добавляет событие в планы и открывает компанию участником.',
SCR('invIn',`<img src="out/raw2/prof.png" style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;object-position:top"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div style="display:flex;gap:12px;align-items:center">${AV('av1',44)}<div><div style="font-size:15.5px">Алина зовёт в компанию «Идём на Techno Room»</div><div class="t-cap" style="margin-top:2px">Сегодня · 23:00 · Re:Public</div></div></div>
<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:14px;display:flex;gap:12px;align-items:center" data-go="event"><div class="ph" style="width:60px;height:60px;border-radius:15px;flex:none"><img src="${I('techno')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px">Techno Room: Ночь 3</div><div class="t-cap" style="margin-top:3px">От 25 BYN · 2 друга уже идут</div></div>${ic('chevR','style="color:var(--text2)"')}</div>
<div style="display:flex;gap:8px;align-items:center;margin-top:12px" class="t-cap">${ic('users','style="width:15px;height:15px"')}Приняв, ты увидишь компанию и чат участников</div>
<div style="display:flex;gap:10px;margin-top:14px"><button class="btn pri" style="flex:1.4" data-go="compMember">Пойду</button><button class="btn sec" style="flex:1" data-go="back">Не смогу</button></div></div>`));

/* ===== MVP · КАРТА «ПРЯМО СЕЙЧАС» ===== */
add('mapNow','pers','40 · Карта · Прямо сейчас','События поблизости: кластеры точек раскрываются тапом, фильтры по времени (сейчас / через час / сегодня вечером), карточка мероприятия снизу и маршрут до него. Построение маршрута передаётся в системные карты.',
`<div class="screen" id="mapNow">${STATUS}
<div style="position:absolute;top:54px;left:0;right:0;padding:0 16px;display:flex;align-items:center;justify-content:space-between"><div class="t-title" style="font-size:22px">Прямо сейчас</div><div class="ibtn" data-go="search">${ic('search')}</div></div>
<div class="chips" style="position:absolute;top:100px;left:16px;right:16px">${[['Сейчас',1],['Через час',0],['Сегодня вечером',0],['Вся неделя',0]].map(([t,o])=>`<div class="chip ${o?'on':''}" style="height:34px;font-size:13.5px">${t}</div>`).join('')}</div>
<div style="position:absolute;top:150px;left:0;right:0;bottom:0;overflow:hidden"><img src="map/minsk_dark.png" style="width:100%;height:100%;object-fit:cover">
<div class="mcluster" style="top:18%;left:30%"><b>7</b><span>техно</span></div>
<div class="mcluster" style="top:26%;left:58%"><b>4</b><span>бары</span></div>
<div class="mcluster" style="top:48%;left:22%"><b>12</b><span>смешанные</span></div>
<div class="mpin" style="top:38%;left:76%"><img src="${I('techno')}" style="width:100%;height:100%;object-fit:cover;border-radius:12px"></div>
<div class="mpin" style="top:60%;left:47%"><img src="${I('wine')}" style="width:100%;height:100%;object-fit:cover;border-radius:12px"></div>
<div style="position:absolute;left:50%;top:64%;width:18px;height:18px;border-radius:9px;background:var(--lime);box-shadow:0 0 0 6px rgba(199,255,40,.18)"></div></div>
<div style="position:absolute;left:12px;right:12px;bottom:112px;background:var(--card);border-radius:24px;padding:12px;display:flex;gap:12px;align-items:center;box-shadow:0 20px 40px rgba(0,0,0,.5)"><div class="ph" style="width:64px;height:64px;border-radius:16px;flex:none" data-go="event"><img src="${I('techno')}"></div><div style="flex:1;min-width:0"><div class="t-h3" style="font-size:15.5px">Techno Room: Ночь 3</div><div class="t-cap" style="margin-top:3px">600 м · идёт сейчас · до 01:00</div></div><button class="btn pri sm" style="height:38px;padding:0 14px" data-go="mapRoute">${ic('route','style="width:16px;height:16px"')}Маршрут</button></div>${UTAB(2)}</div>`,{long:false});

add('mapRoute','pers','41 · Маршрут','Построенный маршрут пешком: время, расстояние и точка сбора. «Открыть в Картах» передаёт маршрут системному приложению — внутри ВРЫВ навигация не хранится.',
SCR('mapRoute',`<div class="screen" id="mapRoute">${STATUS}
${NAV('Маршрут',{back:'mapNow',right:`<span class="lnk" style="color:var(--pink)">Открыть в Картах</span>`})}
<div style="position:absolute;top:104px;left:0;right:0;bottom:0;overflow:hidden"><img src="map/minsk_dark.png" style="width:100%;height:100%;object-fit:cover">
<svg style="position:absolute;inset:0;width:100%;height:100%" viewBox="0 0 393 620" fill="none"><path d="M196 520 C 196 440, 150 400, 150 330 C 150 260, 230 240, 296 150" stroke="#FF1D8E" stroke-width="4" stroke-linecap="round" stroke-dasharray="2 7"/></svg>
<div style="position:absolute;left:47.5%;top:80%;width:18px;height:18px;border-radius:9px;background:var(--lime);box-shadow:0 0 0 6px rgba(199,255,40,.18)"></div>
<div class="mpin" style="top:20%;left:70%"><img src="${I('techno')}" style="width:100%;height:100%;object-fit:cover;border-radius:12px"></div></div>
<div style="position:absolute;left:12px;right:12px;bottom:26px;background:var(--card);border-radius:24px;padding:14px;box-shadow:0 20px 40px rgba(0,0,0,.5)"><div style="display:flex;gap:12px;align-items:center"><div class="lico" style="background:var(--pink);color:#fff">${ic('route')}</div><div><div style="font-size:16px;font-weight:650">15 мин пешком · 1,1 км</div><div class="t-cap" style="margin-top:3px">Re:Public · проспект Независимости, 18</div></div></div>
<div style="display:flex;gap:10px;margin-top:12px"><button class="btn pri" style="flex:1">${ic('link','style="width:17px;height:17px"')}Открыть в Картах</button><button class="btn sec" style="flex:1" data-go="compSearch">${ic('users','style="width:17px;height:17px"')}Позвать с собой</button></div></div>${HI}`));

/* ===== MVP · ПОИСК МЕРОПРИЯТИЙ ===== */
add('search','pers','42 · Поиск мероприятий','Поиск по названию с фильтрами: категория, дата, цена, расстояние. Результаты — списком или на карте, запрос сохраняется. Фильтры мягкие: не подходящее по цене просто ниже в списке.',
SCR('search',`${NAV('Поиск',{back:'homeP'})}<div class="body">
<div class="inp focus" style="height:48px;border-radius:24px">${ic('search')}<span class="sp">techno<span class="caret"></span></span><div class="ibtn" style="width:32px;height:32px;background:transparent">${ic('x','style="width:17px;height:17px"')}</div></div>
<div class="chips" style="margin-top:12px">${[['Клубы',1],['Сегодня',1],['До 50 BYN',1],['До 2 км',1],['Ещё',0]].map(([t,o])=>`<div class="chip ${o?'on':''}" style="height:34px;font-size:13.5px">${t} ${ic('chevD','style="width:13px;height:13px;display:inline;vertical-align:-2px"')}</div>`).join('')}</div>
<div class="seg" style="margin-top:12px;display:flex;background:var(--card);border-radius:14px;padding:3px">${['Списком','На карте'].map((t,i)=>`<div style="flex:1;height:36px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===0?'background:#2E2E37':'color:var(--text2)'}"${g(i?'mapNow':'')}>${t}</div>`).join('')}</div>
<div class="t-cap" style="margin-top:14px">Нашли 12 событий · сортировка: ближе сначала</div>
${[['techno','Techno Room: Ночь 3','Сегодня · 23:00','30 BYN · 600 м'],['dj','Katrin Velvet b2b Nox','Пт · 22:00','35 BYN · 1,2 км'],['techno','Subsonic Night','Вс · 22:00','25 BYN · 2 км'],['wine','Винный вечер с диджеем','Сегодня · 20:00','40 BYN · 900 м']].map(([p,t,d,s])=>`<div class="li noi" style="min-height:72px" data-go="event"><div class="ph" style="width:56px;height:56px;border-radius:14px;flex:none"><img src="${I(p)}"></div><div style="flex:1;min-width:0"><div class="t-h3" style="font-size:15px">${t}</div><div class="t-cap" style="margin-top:3px">${d}</div><div class="t-cap" style="margin-top:2px;color:var(--lime)">${s}</div></div>${ic('chevR','style="color:var(--text2)"')}</div>`).join('')}
</div>${HI}`));

/* ===== MVP · ЦЕНТР УВЕДОМЛЕНИЙ ===== */
add('notifCenter','user','43 · Центр уведомлений','Всё в одном месте: приглашения в компании и на события, сообщения, заявки в друзья, напоминания о планах и изменения мероприятий. Каждое — с действием сразу: принять, ответить, посмотреть, что стало с планом.',
SCR('notifCenter',`${NAV('Уведомления',{back:'homeP',right:'<span class="lnk" style="color:var(--pink)" data-go="back">Прочитать всё</span>'})}<div class="body">
<div class="grp-t">Приглашения · 2</div>
<div class="grp"><div class="li noi" style="min-height:78px" data-go="invIn">${AV('av1',46)}<div><div style="font-size:14.5px">Алина зовёт в компанию «Идём на Techno Room»</div><div class="t-cap" style="margin-top:2px">Сегодня · 23:00 · 2 друга идут</div><div style="display:flex;gap:8px;margin-top:8px"><button class="btn pri sm" style="height:28px;padding:0 12px;font-size:12.5px">Пойду</button><button class="btn sec sm" style="height:28px;padding:0 12px;font-size:12.5px">Не смогу</button></div></div></div>
<div class="li noi" style="min-height:66px" data-go="compMember">${AV('av5',46)}<div><div style="font-size:14.5px">Вика позвала в компанию «Идём на Techno Room»</div><div class="t-cap" style="margin-top:2px">Открытая · 3 из 5 мест</div></div><span class="v" style="color:var(--pink);font-weight:600">Вступить</span></div></div>
<div class="grp-t" style="margin-top:16px">Сообщения</div>
<div class="grp"><div class="li noi" style="min-height:62px" data-go="lchat">${AV('av4',44)}<div><div style="font-size:14.5px">Дима</div><div class="t-cap" style="margin-top:2px">Передал тебе билет, проверь</div></div><span class="dot" style="align-self:center"></span></div></div>
<div class="grp-t" style="margin-top:16px">Заявки в друзья</div>
<div class="grp"><div class="li noi" style="min-height:62px" data-go="friends">${AV('av3',44)}<div><div style="font-size:14.5px">Катя · @katya.ink</div><div class="t-cap" style="margin-top:2px">12 общих друзей</div></div><span class="v" style="gap:6px"><button class="btn sec sm" style="height:30px;padding:0 12px;font-size:13px">Отклонить</button><button class="btn pri sm" style="height:30px;padding:0 12px;font-size:13px">Принять</button></span></div></div>
<div class="grp-t" style="margin-top:16px">Напоминания</div>
<div class="grp"><div class="li noi" style="min-height:62px" data-go="event"><div class="lico">${ic('clock')}</div><div><div style="font-size:14.5px">Techno Room: Ночь 3 · сегодня 23:00</div><div class="t-cap" style="margin-top:2px">Через 3 часа · Re:Public</div></div></div></div>
<div class="grp-t" style="margin-top:16px">Изменения мероприятий</div>
<div class="grp"><div class="li noi" style="min-height:62px" data-go="evMoved"><div class="lico" style="color:var(--pink)">${ic('alert')}</div><div><div style="font-size:14.5px">«Северный Свет» перенесено</div><div class="t-cap" style="margin-top:2px">Новая дата: Сб 24 · 22:00</div></div><span class="v">${ic('chevR')}</span></div>
<div class="li noi" style="min-height:62px" data-go="evCancel"><div class="lico" style="color:var(--pink)">${ic('alert')}</div><div><div style="font-size:14.5px">«Rooftop Sunset Party» отменено</div><div class="t-cap" style="margin-top:2px">План удалён из календаря</div></div><span class="v">${ic('chevR')}</span></div></div>
</div>${HI}`));

/* ===== MVP · СТАТУСЫ МЕРОПРИЯТИЯ ===== */
add('evCancel','states','44 · Событие отменено','Организатор отменил мероприятие: пользователь видит это в планах и уведомлениях, план удаляется автоматически, его действия по билетам — подсказка. Похожие события предлагаются на месте отменённого.',
SCR('evCancel',`${NAV('Планы',{back:'prof'})}<div class="body">
<div style="background:var(--card);border-radius:22px;padding:16px;opacity:.75"><div style="display:flex;gap:12px;align-items:center"><div class="ph" style="width:60px;height:60px;border-radius:15px;flex:none"><img src="${I('rooftop')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px;text-decoration:line-through">Rooftop Sunset Party</div><div class="t-cap" style="margin-top:3px">Сб 24 · 18:00 · Крыша на Немиге</div></div></div><div style="display:flex;justify-content:center;margin-top:12px"><span class="status st-off" style="height:26px"><i></i>Отменено организатором</span></div></div>
<div class="sub" style="margin-top:14px;text-align:center">Событие отменено. Твой план удалён автоматически. Если брал билет — проверь возврат у продавца.</div>
<div class="grp-t" style="margin-top:18px">Похожие в этот вечер</div>
<div class="grp">${[['jazz','Jazz Session','Пт · 20:00 · 25 BYN'],['wine','Винный вечер с диджеем','Сб · 20:00 · 40 BYN']].map(([p,t,s])=>`<div class="li noi" style="min-height:64px" data-go="event"><div class="ph" style="width:48px;height:48px;border-radius:12px;flex:none"><img src="${I(p)}"></div><div><div style="font-size:15px">${t}</div><div class="t-cap" style="margin-top:2px">${s}</div></div>${ic('chevR','style="color:var(--text2)"')}</div>`).join('')}</div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="search">Найти другое событие</button><button class="btn ghost" data-go="back">Убрать из планов</button></div>${HI}`));

add('evMoved','states','45 · Событие перенесено','Перенос показывается парой дат: старая зачёркнута, новая подсвечена. Пользователь подтверждает участие на новую дату или убирает план — без подтверждения план считается неподтверждённым.',
SCR('evMoved',`${NAV('Планы',{back:'prof'})}<div class="body">
<div style="background:var(--card);border-radius:22px;padding:16px"><div style="display:flex;gap:12px;align-items:center"><div class="ph" style="width:60px;height:60px;border-radius:15px;flex:none"><img src="${I('rooftop')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px">Северный Свет: осенний сет</div><div class="t-cap" style="margin-top:3px;text-decoration:line-through">Сб 24 · 20:00</div><div class="t-cap" style="margin-top:2px;color:var(--lime)">Сб 24 · 22:00 · второй зал</div></div></div><div style="display:flex;justify-content:center;margin-top:12px"><span class="status st-wait" style="height:26px"><i></i>Перенесено</span></div></div>
<div class="sub" style="margin-top:14px;text-align:center">Организатор перенёс событие. Подтверди участие на новую дату или убери план.</div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="prof">Иду на новую дату</button><button class="btn ghost" style="color:var(--pink)" data-go="back">Убрать из планов</button></div>${HI}`));

add('evDone','states','46 · Событие завершено','Прошедшее событие отличается от отменённого: план переезжает в «Посетил», доступна оценка и фото. Оценка видна только друзьям.',
SCR('evDone',`${NAV('Планы',{back:'prof'})}<div class="body">
<div style="background:var(--card);border-radius:22px;padding:16px"><div style="display:flex;gap:12px;align-items:center"><div class="ph" style="width:60px;height:60px;border-radius:15px;flex:none"><img src="${I('jazz')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px">Jazz Session</div><div class="t-cap" style="margin-top:3px">Пн 19 · 20:00 · Loft 13</div></div><span class="status st-mute" style="height:24px">Завершено</span></div></div>
<div style="text-align:center;margin-top:20px"><div class="h-big" style="font-size:22px">Как прошло?</div><div class="sub" style="margin-top:6px">Оценка видна только друзьям</div>
<div style="display:flex;gap:10px;justify-content:center;margin-top:16px">${[1,2,3,4,5].map(i=>`<div style="width:44px;height:44px;border-radius:22px;background:var(--card);display:flex;align-items:center;justify-content:center">${ic('star',`style="width:22px;height:22px;${i<4?'fill:var(--lime);stroke:var(--lime)':''}"`)}</div>`).join('')}</div></div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn sec">${ic('camera','style="width:18px;height:18px"')}Добавить фото</button><button class="btn ghost" data-go="search">Похожие события</button></div>${HI}`));

add('evFull','states','47 · Нет свободных мест','Событие не отменяется, но новых мест нет: у плана сохраняется статус, предлагается лист ожидания с пушем при появлении мест и похожие события.',
SCR('evFull',`${NAV('Событие',{back:'event',close:true})}<div class="body">
<div style="background:var(--card);border-radius:22px;padding:16px"><div style="display:flex;gap:12px;align-items:center"><div class="ph" style="width:60px;height:60px;border-radius:15px;flex:none"><img src="${I('techno')}"></div><div style="flex:1"><div class="t-h3" style="font-size:15px">Warehouse Session</div><div class="t-cap" style="margin-top:3px">Сб · 23:00 · Loft 13 · от 25 BYN</div></div><span class="status st-off" style="height:24px">Нет мест</span></div>
<div class="t-cap" style="margin-top:12px;display:flex;gap:8px;align-items:center">${ic('users','style="width:15px;height:15px"')}Билеты закончились · 1 186 человек нажали «Иду»</div></div>
<div class="sub" style="margin-top:14px;text-align:center">Мест нет, но план остаётся: встань в лист ожидания — пришлём пуш, если появятся билеты.</div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="waitOk">В лист ожидания</button><button class="btn ghost" data-go="search">Похожие события</button></div>${HI}`));

/* ===== MVP · ЖАЛОБЫ ===== */
add('report','states','48 · Жалоба','Компактная отправка жалобы на пользователя, сообщение или группу: объект подставляется из точки входа: из компании и её чата — группа, из личного чата — пользователь (на макете — группа компании), причина — чипами, комментарий по желанию. Обработку ведут модераторы на отдельном админ-сайте; в приложении только отправка.',
SCR('report',`<img src="out/raw2/lchat.png" style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;object-position:top"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div style="display:flex;justify-content:space-between;align-items:center"><div class="t-h3">Пожаловаться</div><div class="ibtn" style="width:36px;height:36px;background:transparent" data-go="back">${ic('x','style="width:20px;height:20px"')}</div></div>
<div class="chips" style="margin-top:12px">${[['Пользователь',0],['Сообщение',0],['Группа',1]].map(([t,o])=>`<div class="chip ${o?'on':''}" style="height:34px;font-size:13.5px">${t}</div>`).join('')}</div>
<div class="grp" style="margin-top:12px"><div class="li noi" style="min-height:58px"><div class="lico" style="background:var(--pink);color:#fff">${ic('users','style="width:18px;height:18px"')}</div><div><div style="font-size:15px">Идём на Techno Room</div><div class="t-cap" style="font-size:12.5px">Группа · 4 участника · чат компании</div></div></div></div>
<div class="grp-t">Причина</div>
<div style="display:flex;flex-wrap:wrap;gap:7px">${['Спам','Оскорбления','Фейковое событие','Опасное поведение','18+ без метки'].map((t,i)=>`<span class="chip ${i<2?'sel':''}" style="height:32px;font-size:13.5px">${t}</span>`).join('')}</div>
${FIELD('Комментарий','Что случилось? (необязательно)',{ph:true})}
<div class="t-cap" style="margin-top:10px;padding:0 4px;line-height:1.4">${ic('shield','style="width:14px;height:14px;display:inline;vertical-align:-2px"')} Жалобу рассмотрят модераторы ВРЫВ. Автор не узнает, кто пожаловался.</div>
<div style="display:flex;gap:10px;margin-top:12px"><button class="btn sec" style="flex:1" data-go="back">Отмена</button><button class="btn pri" style="flex:1" data-go="reportOk">Отправить</button></div></div>`));
add('reportOk','states','49 · Жалоба отправлена','Состояние после отправки жалобы: зафиксировано, рассмотрят модераторы на отдельном админ-сайте. Автор жалобы остаётся анонимным; на время проверки ничего не меняется.',
SCR('reportOk',`${NAV('',{back:'back'})}<div class="body" style="text-align:center;padding-top:40px">
<div style="width:72px;height:72px;border-radius:36px;background:var(--card);display:flex;align-items:center;justify-content:center;margin:0 auto">${ic('shield','style="width:34px;height:34px;color:var(--lime)"')}</div>
<div class="h-big" style="margin-top:18px">Жалоба отправлена</div>
<div class="sub" style="max-width:300px;margin:8px auto 0">Модераторы ВРЫВ рассмотрят её на отдельном сайте. Ты останешься анонимным.</div>
<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:20px;display:flex;gap:12px;align-items:center"><div class="lico" style="background:var(--pink);color:#fff">${ic('users','style="width:18px;height:18px"')}</div><div style="flex:1"><div style="font-size:15px">Идём на Techno Room</div><div class="t-cap" style="margin-top:2px">Причина: спам, оскорбления</div></div><span class="status st-wait" style="height:24px"><i></i>На рассмотрении</span></div>
</div><div class="cta"><button class="btn pri" data-go="back">Понятно</button></div>${HI}`));

add('waitOk','states','50 · В листе ожидания','Состояние после вступления в лист ожидания: план сохранён, при появлении мест придёт пуш. До этого карточка события показывает статус «Нет мест».',
SCR('waitOk',`${NAV('',{back:'back'})}<div class="body" style="text-align:center;padding-top:40px">
<div style="width:72px;height:72px;border-radius:36px;background:var(--card);display:flex;align-items:center;justify-content:center;margin:0 auto">${ic('clock','style="width:34px;height:34px;color:var(--lime)"')}</div>
<div class="h-big" style="margin-top:18px">Ты в листе ожидания</div>
<div class="sub" style="max-width:300px;margin:8px auto 0">План сохранён. Пришлём пуш, если появятся билеты.</div>
<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:20px;text-align:left;display:flex;gap:12px;align-items:center"><div class="ph" style="width:48px;height:48px;border-radius:13px;flex:none"><img src="${I('techno')}"></div><div style="flex:1"><div style="font-size:15px">Warehouse Session</div><div class="t-cap" style="margin-top:2px">Сб · 23:00 · Loft 13</div></div><span class="status st-wait" style="height:24px"><i></i>Ждём места</span></div>
</div><div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="search">Похожие события</button><button class="btn ghost" data-go="back">Убрать из листа</button></div>${HI}`));

// === Контекст жалобы: объект подставляется из точки входа (data-rctx) ===
(()=>{
window.RCTX = window.RCTX || 'group';
const G = SCREENS.find(s=>s.id==='report'), K = SCREENS.find(s=>s.id==='reportOk');
const gCard = `<div class="grp" style="margin-top:12px"><div class="li noi" style="min-height:58px"><div class="lico" style="background:var(--pink);color:#fff">${ic('users','style="width:18px;height:18px"')}</div><div><div style="font-size:15px">Идём на Techno Room</div><div class="t-cap" style="font-size:12.5px">Группа · 4 участника · чат компании</div></div></div></div>`;
const uCard = `<div class="grp" style="margin-top:12px"><div class="li noi" style="min-height:58px">${AV('av1',42)}<div><div style="font-size:15px">Алина</div><div class="t-cap" style="font-size:12.5px">@alina.tech</div></div></div></div>`;
const okG = `<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:20px;display:flex;gap:12px;align-items:center"><div class="lico" style="background:var(--pink);color:#fff">${ic('users','style="width:18px;height:18px"')}</div><div style="flex:1"><div style="font-size:15px">Идём на Techno Room</div><div class="t-cap" style="margin-top:2px">Причина: спам, оскорбления</div></div><span class="status st-wait" style="height:24px"><i></i>На рассмотрении</span></div>`;
const okU = `<div style="background:var(--card);border-radius:20px;padding:14px;margin-top:20px;display:flex;gap:12px;align-items:center">${AV('av1',42)}<div style="flex:1"><div style="font-size:15px">Алина</div><div class="t-cap" style="margin-top:2px">Причина: спам, оскорбления</div></div><span class="status st-wait" style="height:24px"><i></i>На рассмотрении</span></div>`;
const gh = G.html, kh = K.html;
const uh = gh
 .replace('<div class="chip " style="height:34px;font-size:13.5px">Пользователь</div>','<div class="chip on" style="height:34px;font-size:13.5px">Пользователь</div>')
 .replace('<div class="chip on" style="height:34px;font-size:13.5px">Группа</div>','<div class="chip " style="height:34px;font-size:13.5px">Группа</div>')
 .replace(gCard,uCard);
const kuh = kh.replace(okG, okU);
if (uh===gh) console.warn('report: варианты не различаются');
if (kuh===kh) console.warn('reportOk: варианты не различаются');
Object.defineProperty(G,'html',{configurable:true,get(){return window.RCTX==='user'?uh:gh}});
Object.defineProperty(K,'html',{configurable:true,get(){return window.RCTX==='user'?kuh:kh}});
})();
