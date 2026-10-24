/* ===== ПЕРСОНАЛЬНАЯ АФИША ===== */
const MC=(p,t,s,m,go='event')=>`<div class="mcard" style="width:150px;flex:none"${g(go)}><div class="ph" style="height:180px;border-radius:22px"><img src="${I(p)}">${m?`<div class="in" style="top:10px;left:10px"><span class="tag lime">${m}</span></div>`:''}</div><div style="font-size:15px;font-weight:620;margin:9px 2px 2px;line-height:1.25">${t}</div><div class="t-cap" style="margin:0 2px">${s}</div></div>`;
const SH=(t,a='Все',extra='')=>`<div class="sec-h" style="margin:26px 0 12px"><span class="t-h2" style="font-size:20px">${t}</span>${extra}<a>${a}</a></div>`;
add('homeP','pers','Персональная афиша','Первый экран после онбординга и при каждом входе. Блоки собираются по сигналам: интересы, бюджет, лайки/пропуски, сохранения, «Иду», расстояние (если разрешена геолокация), удобное время, планы друзей (только если друзья открыли их). Проценты совместимости в MVP не показываем — на карточках общие интересы. Пустой блок не показывается.',
`<div class="screen long" id="homeP">${STATUS}
<div class="hdr" style="position:absolute;top:54px;left:0;right:0;height:52px;display:flex;align-items:center;justify-content:space-between;padding:0 16px"><img src="ph/logo.png" style="height:42px;margin-top:-4px"><div style="display:flex;gap:8px;align-items:center"><div class="chip" style="height:36px">${ic('pin','style="width:16px;height:16px;color:var(--pink)"')}Минск</div><div class="ibtn" data-go="search">${ic('search')}</div><div class="ibtn" style="position:relative" data-go="notifCenter">${ic('bell')}<i class="dot" style="position:absolute;top:9px;right:10px;width:7px;height:7px"></i></div></div></div>
<div style="padding:116px 16px 0"><div class="t-cap" style="margin-bottom:6px">Привет, Максим · четверг, 22 октября</div><div class="t-display">Для тебя</div></div>
<div class="chips" style="margin-top:16px"><div class="chip on">Всё</div><div class="chip">Сегодня</div><div class="chip">Выходные</div><div class="chip out" data-go="filter">${ic('sliders','style="width:15px;height:15px"')}До 50 BYN</div></div>
<div class="ph scrim" style="margin:16px 16px 0;height:262px;border-radius:28px" data-go="event"><img src="${I('techno')}">
 <div class="in" style="top:14px;left:14px;right:14px;display:flex;justify-content:space-between;align-items:center"><span class="tag glass">Сегодня · 23:00</span><div class="ibtn glass">${ic('heart')}</div></div>
 <div class="in" style="left:18px;right:18px;bottom:18px"><span class="tag lime">Техно</span><span class="tag lime">Андеграунд</span>
 <div class="t-title" style="font-size:27px;margin-top:10px">Techno Room: Ночь 3</div><div class="t-cap" style="color:rgba(245,245,247,.75);margin-top:6px">Re:Public · Техно, андеграунд · 30 BYN</div></div></div>
${SH('Потому что тебе нравится техно')}
<div class="hscroll">${MC('concert','Warehouse Session','Сб · 23:00 · 35 BYN','Техно')}${MC('dj','Katrin Velvet b2b Nox','Пт · 22:00 · Loft Club','Хаус')}${MC('techno','Subsonic Night','Вс · 22:00 · 25 BYN','Техно')}</div>
${SH('Твои друзья собираются')}
<div style="margin:0 16px;background:var(--card);border-radius:22px;padding:10px;display:flex;gap:12px;align-items:center" data-go="event"><div class="ph" style="width:72px;height:72px;border-radius:15px;flex:none"><img src="${I('rooftop')}"></div><div style="flex:1;min-width:0"><div class="t-h3" style="font-size:15.5px">Rooftop Sunset Party</div><div class="t-cap" style="margin-top:3px">Сб · 18:00 · Крыша на Немиге</div><div style="display:flex;align-items:center;gap:8px;margin-top:8px"><div class="stack oncard">${AV('av1',24)}${AV('av5',24)}${AV('av4',24)}</div><span style="font-size:12.5px;font-weight:600">Алина, Вика и Дима идут</span></div></div></div>
${SH('Популярно рядом','На карте',' data-go="mapNow"')}
<div class="hscroll">${[['streetfood','Фуд-маркет','600 м · сейчас'],['wine','Винный вечер','1,4 км · до 01:00'],['jazz','Jazz Session','1,2 км · 20:00']].map(([p,t,s])=>`<div style="width:240px;flex:none;background:var(--card);border-radius:20px;padding:8px;display:flex;gap:10px;align-items:center"><div class="ph" style="width:60px;height:60px;border-radius:13px;flex:none"><img src="${I(p)}"></div><div><div class="t-h3" style="font-size:15px">${t}</div><div class="t-cap" style="margin-top:3px">${s}</div></div></div>`).join('')}</div>
${SH('Похожие на сохранённые')}
<div class="hscroll">${MC('gallery','«Тихий город» в Ў','До 30 нояб · 0 BYN','Выставки')}${MC('standup','Открытый микрофон','Сб · 19:30 · 15 BYN','Стендап')}${MC('cinema','Кино в Парке Горького','Вс · 20:30 · 10 BYN','Кино')}</div>
<div style="margin:22px 16px 0;display:flex;gap:8px;align-items:center" class="t-cap">${ic('spark','style="width:16px;height:16px"')}Подборка учитывает твои интересы, бюджет и лайки. <span class="link">Настроить</span></div>
${UTAB(0)}</div>`,{long:true});


add('filter','pers','Афиша · Фильтры','Шторка поверх афиши. Фильтр — временный и мягкий: скрывает только то, что точно не подходит (цена «неважно» не скрывает дорогое — оно просто ниже). «Показать · N» обновляет ленту сразу, крестик закрывает без применения. Выбор сохраняется до конца сессии.',
SCR('filter',`<img src="out/raw2/homeP.png" style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;object-position:top"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div style="display:flex;justify-content:space-between;align-items:center"><div class="t-h3">Фильтры</div><div class="ibtn" style="width:36px;height:36px;background:transparent" data-go="back">${ic('x','style="width:20px;height:20px"')}</div></div>
<div class="grp-t" style="margin-top:6px">Когда</div><div style="display:flex;flex-wrap:wrap;gap:7px">${[['Сегодня',1],['Завтра',0],['Выходные',0],['На неделе',0],['Любой день',0]].map(([t,o])=>`<div class="chip ${o?'on':''}" style="height:32px;font-size:13.5px">${t}</div>`).join('')}</div>
<div class="grp-t">Цена</div><div style="display:flex;flex-wrap:wrap;gap:7px">${[['Free',0],['До 20',0],['До 50',1],['100+',0],['Неважно',0]].map(([t,o])=>`<div class="chip ${o?'on':''}" style="height:32px;font-size:13.5px">${t}</div>`).join('')}</div>
<div class="grp-t">Район</div><div style="display:flex;flex-wrap:wrap;gap:7px">${[['Центр',1],['Немига',1],['Октябрьская',0],['Зыбицкая',0],['Рядом',0]].map(([t,o])=>`<div class="chip ${o?'on':''}" style="height:32px;font-size:13.5px">${t}</div>`).join('')}</div>
<div class="grp" style="margin-top:16px">${LI('users','Только с друзьями','',{tg:false})}${LI('zap','Прямо сейчас','',{tg:false})}</div>
<div style="display:flex;gap:10px;margin-top:14px"><button class="btn sec" style="flex:1" data-go="homeP">Сбросить</button><button class="btn pri" style="flex:1.6" data-go="homeP">Показать · 128</button></div></div>`));

/* ===== ПРОФИЛЬ ===== */
const days2=[['Пн',19,'v'],['Вт',20,''],['Ср',21,'c'],['Чт',22,''],['Пт',23,'g'],['Сб',24,'g'],['Вс',25,'']];
add('prof','user','10 · Профиль (расширенный)','Шапка: фото, имя, @username (предложен из имени при первом входе), город, о себе, счётчики. Вкладки Планы / Сохранённое / Друзья. В календаре три статуса: Собираюсь (розовый), Посетил (серый), Отменил (перечёркнуто). Свайп по плану влево — «Убрать из планов». Планы по умолчанию видны только мне. Переключатель «Личный ↔ Бизнес» — пункт «ВРЫВ для бизнеса» и шеврон у имени.',
`<div class="screen long" id="prof">${STATUS}
<div class="hdr" style="position:absolute;top:54px;left:0;right:0;height:52px;display:flex;align-items:center;justify-content:space-between;padding:0 16px"><div style="display:flex;align-items:center;gap:6px" data-go="sw"><span class="t-h3">@max.vryv</span>${ic('chevD','style="width:18px;height:18px;color:var(--text2)"')}</div><div style="display:flex;gap:8px"><div class="ibtn">${ic('share','style="width:20px;height:20px"')}</div><div class="ibtn" data-go="settings">${ic('gear','style="width:20px;height:20px"')}</div></div></div>
<div style="padding:116px 16px 0;display:flex;gap:16px;align-items:center">${AV('av2',84,'box-shadow:0 0 0 2px var(--bg),0 0 0 4px var(--pink)')}<div style="flex:1"><div class="t-title">Максим</div><div class="t-cap" style="margin-top:3px">${ic('pin','style="width:13px;height:13px;display:inline;vertical-align:-2px"')} Минск</div></div></div>
<div style="padding:14px 16px 0;font-size:15px;line-height:1.45">Техно по пятницам, стендап по средам. Ищу компанию на Warehouse.</div>
<div style="display:flex;gap:8px;padding:16px 16px 0">${[['86','Друзей'],['17','Сохранено'],['48','Посетил']].map(([n,l])=>`<div class="stat" style="flex:1;background:var(--card);border-radius:18px;padding:12px 14px"><b style="font-size:21px;font-weight:720;display:block">${n}</b><span class="t-cap">${l}</span></div>`).join('')}</div>
<div style="display:flex;gap:8px;padding:12px 16px 0"><button class="btn sec" style="flex:1;height:44px;font-size:15px" data-go="edit">Редактировать профиль</button><div class="ibtn" style="width:44px;height:44px;border-radius:22px" data-go="recs">${ic('sliders')}</div></div>
<div style="margin:12px 16px 0;background:var(--card);border-radius:20px;padding:12px 14px;display:flex;gap:12px;align-items:center" data-go="biz"><div class="oico" style="color:var(--lime)">${ic('store')}</div><div style="flex:1"><div class="t-h3" style="font-size:15px">ВРЫВ для бизнеса</div><div class="t-cap" style="margin-top:2px">Заведение или свои события</div></div>${ic('chevR','style="color:var(--text2)"')}</div>
<div class="seg" style="margin:20px 16px 0;display:flex;background:var(--card);border-radius:14px;padding:3px">${['Планы','Сохранённое','Друзья'].map((t,i)=>`<div style="flex:1;height:34px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===0?'background:#2E2E37':'color:var(--text2)'}"${g(['prof','saved','friends'][i])}>${t}</div>`).join('')}</div>
<div class="cal" style="margin:14px 16px 0;background:var(--card);border-radius:24px;padding:14px 12px 12px">
 <div style="display:flex;justify-content:space-between;align-items:center;padding:0 4px"><span class="t-h3" style="font-size:15px">Октябрь 2026</span><span style="display:flex;gap:10px;font-size:11.5px;color:var(--text2);font-weight:600"><span><i class="dot" style="display:inline-block;width:6px;height:6px;margin-right:4px"></i>Иду</span><span><i class="dot" style="display:inline-block;width:6px;height:6px;margin-right:4px;background:#6A6A76"></i>Был</span><span><i class="dot" style="display:inline-block;width:6px;height:6px;margin-right:4px;background:transparent;box-shadow:inset 0 0 0 1.2px #6A6A76"></i>Отменил</span></span></div>
 <div style="display:flex;justify-content:space-between;margin-top:12px">${days2.map(([d,n,e])=>`<div style="width:42px;height:62px;border-radius:15px;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:4px;${n===24?'background:var(--pink)':''}"><span style="font-size:11px;font-weight:600;color:${n===24?'rgba(255,255,255,.8)':'var(--text2)'}">${d}</span><b style="font-size:17px;font-weight:650">${n}</b><i style="width:5px;height:5px;border-radius:3px;display:block;${n===24?'background:#fff':e==='g'?'background:var(--pink)':e==='v'?'background:#6A6A76':e==='c'?'box-shadow:inset 0 0 0 1.2px #6A6A76':''}"></i></div>`).join('')}</div>
 <div style="border-top:1px solid var(--line);margin-top:12px;padding:4px 4px 0">
 ${[['rooftop','Rooftop Sunset Party','Сб 24 · 18:00 · Крыша на Немиге','<span class="status st-pink"><i></i>Собираюсь</span>',''],['concert','Северный Свет','Сб 24 · 20:00 · с группой из 4','<span class="status st-pink"><i></i>Собираюсь</span>',''],['standup','Открытый микрофон','Ср 21 · 19:30','<span class="status st-mute">Отменил</span>','opacity:.55;text-decoration:line-through'],['jazz','Jazz Session','Пн 19 · 20:00','<span class="status st-ok"><i></i>Посетил</span>','']].map(([p,t,s,st,sty])=>`<div style="display:flex;gap:12px;align-items:center;padding:9px 0" data-go="event"><div class="ph" style="width:52px;height:52px;border-radius:13px;flex:none"><img src="${I(p)}"></div><div style="flex:1;min-width:0"><div class="t-h3" style="font-size:15px;${sty}">${t}</div><div class="t-cap" style="margin-top:2px">${s}</div></div>${st}</div>`).join('')}
 </div></div>
<div style="margin:12px 16px 0;display:flex;gap:10px;align-items:center;padding:0 4px" class="t-cap">${ic('lock','style="width:16px;height:16px"')}Мои планы видят: <b style="color:var(--text)">только друзья</b> <span class="link" data-go="edit">Изменить</span></div>
${UTAB(4)}</div>`,{long:true});

add('saved','user','10 · Профиль · Сохранённое','Коллекция лайков и закладок. Сортировка: дата (ближайшие), цена, категория. Прошедшие события уходят в «Архив» автоматически. Пустое состояние предлагает открыть свайпы.',
SCR('saved',`${NAV('Сохранённое',{back:'prof',right:`<div class="ibtn" style="background:transparent">${ic('search')}</div>`})}<div class="body" style="padding:0">
<div class="chips"><div class="chip on" data-go="sort">По дате ${ic('chevD','style="width:14px;height:14px"')}</div><div class="chip">По цене</div><div class="chip">Категория</div><div class="chip">Архив</div></div>
<div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;padding:16px 16px 0">${[['techno','Techno Room: Ночь 3','Сегодня · 30 BYN'],['rooftop','Rooftop Sunset','Сб · 40 BYN'],['jazz','Jazz Session','Пт · 25 BYN'],['gallery','Тихий город','До 30.11 · 0 BYN'],['standup','Открытый микрофон','Сб · 15 BYN'],['run','Забег у реки','Вс · 0 BYN']].map(([p,t,s])=>`<div data-go="event"><div class="ph" style="height:180px;border-radius:22px"><img src="${I(p)}"><div class="in" style="top:10px;right:10px">${icf('bookmark','#FF1D8E').replace('class="ico"','class="ico" style="width:20px;height:20px;fill:#FF1D8E;stroke:#FF1D8E"')}</div></div><div style="font-size:14.5px;font-weight:620;margin:8px 2px 2px">${t}</div><div class="t-cap" style="margin:0 2px;font-size:12.5px">${s}</div></div>`).join('')}</div>
</div><div class="fade-bottom"></div>${UTAB(4)}`));

add('friends','user','10 · Профиль · Друзья','Заявки сверху (принять / отклонить). Поиск по @username и имени. «Друзья идут» показывает события только тех, кто открыл свои планы. Приглашение на событие отправляется из карточки события или отсюда.',
SCR('friends',`${NAV('Друзья',{back:'prof',right:`<div class="ibtn" style="background:transparent">${ic('plus')}</div>`})}<div class="body">
<div class="searchbar" style="height:46px;border-radius:23px;background:var(--card);display:flex;align-items:center;gap:10px;padding:0 16px;color:var(--text2);font-size:16px">${ic('search','style="width:19px;height:19px"')}Найти по имени или @username</div>
<div class="grp-t" style="margin-top:18px">Заявки · 2</div>
${[['av3','Катя','@katya.ink · 12 общих'],['av6','Артём','@artem_jazz · 4 общих']].map(([a,n,s])=>`<div style="display:flex;gap:12px;align-items:center;padding:8px 0">${AV(a,48)}<div style="flex:1"><div class="t-h3" style="font-size:15px">${n}</div><div class="t-cap" style="margin-top:2px">${s}</div></div><button class="btn pri sm" style="height:34px;padding:0 14px">Принять</button><div class="ibtn" style="width:34px;height:34px">${ic('x','style="width:16px;height:16px"')}</div></div>`).join('')}
<div class="grp-t" style="margin-top:14px">Приглашения</div>
<div style="background:var(--card);border-radius:20px;padding:10px;display:flex;gap:12px;align-items:center"><div class="ph" style="width:52px;height:52px;border-radius:13px;flex:none"><img src="${I('concert')}"></div><div style="flex:1"><div style="font-size:14px;font-weight:600">Алина зовёт на «Северный Свет»</div><div class="t-cap" style="margin-top:2px">Сб 24 · 20:00</div></div><div class="ibtn" style="background:var(--pink);width:36px;height:36px" data-go="invIn">${ic('check','style="width:18px;height:18px;stroke-width:2.4"')}</div></div>
<div class="grp-t" style="margin-top:14px">Мои друзья · 86</div>
${[['av1','Алина','Идёт на Rooftop Sunset · Сб'],['av5','Вика','Планы скрыты'],['av4','Дима','Идёт на Северный Свет · Сб']].map(([a,n,s])=>`<div style="display:flex;gap:12px;align-items:center;padding:8px 0">${AV(a,48)}<div style="flex:1"><div class="t-h3" style="font-size:15px">${n}</div><div class="t-cap" style="margin-top:2px">${s}</div></div><span data-go="lchat">${ic('chat','style="color:var(--text2)"')}</span></div>`).join('')}
</div><div class="fade-bottom"></div>${UTAB(4)}`));

add('edit','user','11 · Редактирование профиля','Username проверяется на лету (debounce 400 мс): свободен — лайм, занят — розовый с вариантами. О себе — до 150 символов. Видимость планов: Только я (по умолчанию) / Друзья / Все. Изменения сохраняются кнопкой «Готово»; при выходе без сохранения — подтверждение.',
SCR('edit',`${NAV('Профиль',{back:'prof',right:'<span class="lnk" style="color:var(--pink)" data-go="prof">Готово</span>'})}<div class="body">
<div style="display:flex;flex-direction:column;align-items:center;margin-top:4px"><div style="position:relative">${AV('av2',92)}<div style="position:absolute;right:-2px;bottom:-2px;width:32px;height:32px;border-radius:50%;background:var(--pink);display:flex;align-items:center;justify-content:center;box-shadow:0 0 0 3px var(--bg)">${ic('camera','style="width:17px;height:17px"')}</div></div><span class="link" style="font-size:14px;margin-top:10px">Изменить фото</span></div>
${FIELD('Имя','Максим')}
${FIELD('Username','@max.vryv',{st:'ok',right:icf('check','#C7FF28').replace('class="ico"','class="ico" style="width:18px;height:18px;fill:none;stroke:#C7FF28;stroke-width:2.6"'),hint:'Свободен',hst:'ok'})}
${FIELD('О себе','Техно по пятницам, стендап по средам. Ищу компанию на Warehouse.',{area:true})}
<div class="hint" style="justify-content:flex-end">62 / 150</div>
<div class="grp-t">Кто видит мои планы</div>
<div style="display:flex;background:var(--card);border-radius:14px;padding:3px">${['Только я','Друзья','Все'].map((t,i)=>`<div style="flex:1;height:36px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===1?'background:#2E2E37':'color:var(--text2)'}">${t}</div>`).join('')}</div>
</div>${HI}`));

const chipsOf=(arr,on)=>arr.map(t=>`<div class="chip ${on.includes(t)?'sel':''}" style="height:32px;font-size:13.5px">${t}</div>`).join('');
add('recs','user','12 · Настройки рекомендаций','Все ответы онбординга редактируются здесь, плюс районы и время. Переключатели учитывают геолокацию и планы друзей. «Сбросить персонализацию» стирает поведенческие сигналы (лайки/пропуски) и снова показывает онбординг — с подтверждением.',
`<div class="screen long" id="recs">${STATUS}${NAV('Мои интересы',{back:'prof'})}<div style="padding:106px 16px 0">
<div class="grp-t" style="margin-top:4px">Жанры и тематика</div><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['Техно','Хаус','Концерты','Рок','Хип-хоп','Стендап','Бары','Клубы','Выставки','Кино','Квизы','Лекции','Андеграунд'],['Техно','Хаус','Стендап','Выставки','Андеграунд'])}<div class="chip out" style="height:32px;font-size:13.5px">Ещё 5</div></div>
<div class="grp-t">Бюджет</div><div style="display:flex;background:var(--card);border-radius:14px;padding:3px">${['0','20','50','100','∞'].map((t,i)=>`<div style="flex:1;height:36px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===2?'background:var(--pink)':'color:var(--text2)'}">${t==='∞'?'Неважно':t==='0'?'Free':'до '+t}</div>`).join('')}</div>
<div class="grp-t">Формат отдыха</div><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['С друзьями','Новые знакомства','Один','Спонтанно','Планирую заранее','Спокойные места','Ночная жизнь'],['С друзьями','Новые знакомства','Спонтанно','Ночная жизнь'])}</div>
<div class="grp-t">Районы</div><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['Центр','Немига','Октябрьская','Зыбицкая','Уручье','Каменная Горка'],['Центр','Немига','Октябрьская'])}</div>
<div class="grp-t">Удобное время</div><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['Будни вечер','Пятница','Суббота','Воскресенье днём','Ночь'],['Пятница','Суббота','Ночь'])}</div>
<div class="grp" style="margin-top:22px">${LI('locate','Учитывать расстояние','',{tg:true})}${LI('users','Учитывать планы друзей','',{tg:true})}${LI('bell','Пуши «Прямо сейчас»','',{tg:false})}</div>
<div class="grp">${LI('refresh','Сбросить персонализацию','',{danger:true,chev:false,go:'p1'})}</div>
<div class="t-cap" style="padding:0 4px;line-height:1.4">Сброс удалит лайки, пропуски и историю. Сохранённые события и планы останутся.</div>
</div>${HI}</div>`,{long:true});

add('settings','user','13 · Настройки аккаунта','Группы iOS-списков. Пароль показывается, только если используется вход по Email; иначе — «Создать пароль». Google можно отвязать, только если есть пароль (иначе потеряется доступ). Сессии: список устройств с «Завершить». Удаление — 30 дней на восстановление, с повторным подтверждением.',
`<div class="screen long" id="settings">${STATUS}${NAV('Настройки',{back:'prof'})}<div style="padding:106px 16px 0">
<div class="grp" style="margin-top:4px"><div class="li noi" style="min-height:72px" data-go="edit">${AV('av2',46)}<div><div class="t-h3" style="font-size:16px">Максим</div><div class="t-cap" style="margin-top:2px">@max.vryv</div></div><span class="v">${ic('chevR')}</span></div></div>
<div class="grp-t">Аккаунт</div><div class="grp">${LI('mail','Email','max.kovalev@gmail.com',{go:'mail'})}${LI('lock','Пароль','Изменён 2 мес. назад',{go:'pass'})}<div class="li"><div class="lico" style="background:#F5F5F7">${GOOGLE.replace('width="20" height="20"','width="18" height="18"')}</div><span>Google</span><span class="v"><span class="status st-ok" style="height:22px"><i></i>Подключён</span>${ic('chevR')}</span></div>${LI('device','Активные сессии','2 устройства',{go:'sessions'})}</div>
<div class="grp-t">Приложение</div><div class="grp">${LI('bell','Уведомления','',{go:'notif'})}${LI('lock','Конфиденциальность','Планы: друзья',{go:'priv'})}${LI('locate','Разрешения','Геолокация · вкл',{go:'perms'})}${LI('sliders','Мои интересы','',{go:'recs'})}</div>
<div class="grp-t">Бизнес</div><div class="grp">${LI('store','ВРЫВ для бизнеса','Loft 13',{go:'sw'})}</div>
<div class="grp">${LI('logout','Выйти','',{danger:true,chev:false,go:'welcome'})}${LI('trash','Удалить аккаунт','',{danger:true,chev:false,go:'delacc'})}</div>
<div class="t-cap" style="text-align:center;font-size:12px">ВРЫВ 1.0 · Пользовательское соглашение · Политика</div>
</div>${HI}</div>`,{long:true});

add('sessions','user','13 · Активные сессии','Каждое устройство — отдельная refresh-сессия. «Завершить» отзывает токен мгновенно. «Выйти везде, кроме этого» — после смены пароля или при подозрении на взлом.',
SCR('sessions',`${NAV('Активные сессии',{back:'settings'})}<div class="body">
<div class="grp-t" style="margin-top:4px">Это устройство</div><div class="grp"><div class="li" style="min-height:66px"><div class="lico" style="color:var(--lime)">${ic('device')}</div><div><div style="font-size:15px">iPhone 16 Pro</div><div class="t-cap" style="font-size:12.5px">Минск · сейчас · Google</div></div></div></div>
<div class="grp-t">Другие</div><div class="grp"><div class="li" style="min-height:66px"><div class="lico">${ic('device')}</div><div><div style="font-size:15px">iPhone 13</div><div class="t-cap" style="font-size:12.5px">Минск · 3 дня назад · Email</div></div><span class="v" style="color:var(--pink);font-weight:600">Завершить</span></div></div>
<button class="btn sec" style="margin-top:12px;color:var(--pink)">Выйти везде, кроме этого</button>
</div>${HI}`));

/* ===== НАСТРОЙКИ · НОВЫЕ ЭКРАНЫ ===== */
add('mail','user','13 · Смена Email','Смена в два шага: код на новый адрес, старый Email работает до подтверждения. Если новый адрес уже занят — ошибка с предложлением войти, дубль не создаётся. На оба адреса приходит уведомление об изменении.',
SCR('mail',`${NAV('Смена Email',{back:'settings'})}<div class="body">
<div class="h-big" style="margin-top:6px">Новый Email</div><div class="sub">Пришлём код на новый адрес. Старый Email будет работать, пока ты его не подтвердишь.</div>
${FIELD('Текущий Email','max.kovalev@gmail.com',{icon:'mail'})}
${FIELD('Новый Email','max.kovalev@icloud.com',{icon:'mail',st:'ok',right:icf('check','#C7FF28').replace('class="ico"','class="ico" style="width:18px;height:18px;fill:none;stroke:#C7FF28;stroke-width:2.6"')})}
${FIELD('Пароль','••••••••••',{icon:'lock',right:ic('eye')})}
<div class="grp" style="margin-top:16px"><div class="li"><div class="lico">${ic('shield')}</div><div style="font-size:13.5px;color:var(--text2);font-weight:500;line-height:1.4;padding:12px 0">Если новый адрес уже зарегистрирован, предложим войти в тот аккаунт — дубль не создаётся.</div></div></div>
</div><div class="cta"><button class="btn pri" data-go="mailCode">Отправить код</button></div>${HI}`));

add('mailCode','user','13 · Смена Email · код','Шаг подтверждения: тот же компонент ввода кода, что при регистрации. Срок действия — 10 минут, 5 попыток. Автоподстановка из письма. Ввод последней цифры сразу отправляет код.',
SCR('mailCode',`${NAV('Смена Email',{back:'mail'})}<div class="body">
<div style="width:60px;height:60px;border-radius:20px;background:var(--card);display:flex;align-items:center;justify-content:center;margin-top:8px;color:var(--pink)">${ic('mail','style="width:28px;height:28px"')}</div>
<div class="h-big" style="margin-top:22px">Проверь почту</div><div class="sub">Отправили код на <b style="color:var(--text)">max.kovalev@icloud.com</b>. <span class="link" data-go="mail">Изменить</span></div>
<div class="code" data-go="mailDone"><div>3</div><div>1</div><div>5</div><div class="f"><span class="caret" style="height:28px"></span></div><div></div><div></div></div>
<div class="t-cap" style="margin-top:22px;text-align:center">Отправить снова через <b style="color:var(--text)">0:47</b></div>
<div class="t-cap" style="margin-top:8px;text-align:center;font-size:12px">Код действует 10 минут. Не пришёл — проверь «Спам».</div>
</div><div class="cta"><button class="btn dis">Подтвердить</button></div>${HI}`));

add('mailDone','user','13 · Email изменён','Успех без лишних шагов: сессия сохранена, вход уже доступен по новому адресу. На старый и новый Email отправлены уведомления об изменении — так безопаснее при взломе.',
SCR('mailDone',`<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;padding:0 32px;text-align:center"><div style="width:84px;height:84px;border-radius:28px;background:var(--lime);display:flex;align-items:center;justify-content:center;color:#0B0B0F">${ic('check','style="width:40px;height:40px;stroke-width:2.6"')}</div>
<div class="h-big" style="margin-top:28px">Email изменён</div><div class="sub">Теперь вход по <b style="color:var(--text)">max.kovalev@icloud.com</b>. На оба адреса отправили уведомление.</div></div>
<div class="cta"><button class="btn pri" data-go="settings">Готово</button></div>${HI}`));

add('pass','user','13 · Смена пароля','Те же требования, что при регистрации, плюс «не совпадает со старым». После смены все сессии, кроме текущего устройства, завершаются; на почту приходит уведомление. Проверка требований идёт по мере ввода.',
SCR('pass',`${NAV('Смена пароля',{back:'settings'})}<div class="body">
<div class="h-big" style="margin-top:6px">Новый пароль</div><div class="sub">После смены выйдем из аккаунта на других устройствах.</div>
${FIELD('Текущий пароль','••••••••••',{icon:'lock',right:ic('eye')})}
${FIELD('Новый пароль','••••••••',{icon:'lock',st:'ok',right:ic('eye')})}
<div style="display:flex;gap:5px;margin:10px 4px 0">${[1,1,1,1].map(()=>`<i style="flex:1;height:4px;border-radius:2px;background:var(--lime);display:block"></i>`).join('')}</div>
<div style="display:flex;flex-wrap:wrap;gap:4px 14px;margin:8px 4px 0;font-size:12.5px;font-weight:550;color:var(--lime)"><span>✓ 8+ символов</span><span>✓ буква и цифра</span><span>✓ не как старый</span></div>
${FIELD('Повтори пароль','••••••••',{st:'ok',hint:'Пароли совпадают',hst:'ok'})}
</div><div class="cta"><button class="btn pri" data-go="passOk">Изменить пароль</button></div>${HI}`));

add('passOk','user','13 · Пароль изменён','Подтверждение с объяснением последствий: другие устройства вышли из аккаунта, на почту отправлено уведомление. Кнопка возвращает в настройки.',
SCR('passOk',`<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;padding:0 32px;text-align:center"><div style="width:84px;height:84px;border-radius:28px;background:var(--lime);display:flex;align-items:center;justify-content:center;color:#0B0B0F">${ic('check','style="width:40px;height:40px;stroke-width:2.6"')}</div>
<div class="h-big" style="margin-top:28px">Пароль изменён</div><div class="sub">Другие устройства вышли из аккаунта. На почту отправили уведомление.</div></div>
<div class="cta"><button class="btn pri" data-go="settings">Готово</button></div>${HI}`));

add('notif','user','13 · Уведомления','Группы переключателей по смыслу: события, друзья, заведения, каналы. Тихие часы 23:00–9:00 откладывают всё, кроме напоминаний о планах. Каждая группа выключается целиком из шапки.',
SCR('notif',`${NAV('Уведомления',{back:'settings'})}<div class="body">
<div class="grp-t" style="margin-top:4px">События</div><div class="grp">${LI('bell','Напоминания о планах','за 3 часа',{tg:true})}${LI('zap','«Прямо сейчас»','за 2–3 часа',{tg:true})}${LI('calendar','Изменения событий','перенос и отмена',{tg:true})}</div>
<div class="grp-t">Друзья</div><div class="grp">${LI('users','Заявки в друзья','',{tg:true})}${LI('send','Приглашения на события','',{tg:false})}</div>
<div class="grp-t">Заведения</div><div class="grp">${LI('store','Новости подписок','раз в день',{tg:true})}</div>
<div class="grp-t">Каналы</div><div class="grp">${LI('device','Push-уведомления','',{tg:true})}${LI('mail','Email-дайджест','раз в неделю',{tg:true})}</div>
<div style="display:flex;gap:10px;align-items:flex-start;margin-top:14px;padding:0 4px"><span style="color:var(--text2)">${ic('moon','style="width:17px;height:17px"')}</span><span class="t-cap" style="line-height:1.4">Тихие часы 23:00–9:00: откладываем всё, кроме напоминаний о планах.</span></div>
</div>${HI}`));

add('priv','user','13 · Конфиденциальность','Видимость планов — как в редактировании профиля. Заведения получают только обезличенные агрегаты: возраст диапазонами, районы, интересы, время активности. Имена, история свайпов и точная геолокация не передаются никому.',
SCR('priv',`${NAV('Конфиденциальность',{back:'settings'})}<div class="body">
<div class="grp-t" style="margin-top:4px">Кто видит мои планы</div>
<div style="display:flex;background:var(--card);border-radius:14px;padding:3px">${['Только я','Друзья','Все'].map((t,i)=>`<div style="flex:1;height:36px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===1?'background:#2E2E37':'color:var(--text2)'}">${t}</div>`).join('')}</div>
<div class="grp-t">Данные</div><div class="grp">${LI('chart','Учитывать «Иду» в статистике','анонимно',{tg:true})}${LI('locate','Учитывать расстояние','если включена геолокация',{tg:true})}${LI('users','Показывать планы друзьям','',{tg:true})}</div>
<div class="grp-t">Что видят заведения</div>
<div style="background:var(--card);border-radius:20px;padding:14px 16px;display:flex;gap:12px;align-items:flex-start"><span style="color:var(--lime)">${ic('shield','style="width:19px;height:19px"')}</span><span class="t-cap" style="line-height:1.45">Только обезличенные агрегаты: возраст диапазонами, районы, интересы, время активности. Имена, история свайпов и точная геолокация не передаются. Группы меньше 20 человек не показываются.</span></div>
</div>${HI}`));

add('perms','user','13 · Разрешения','Разрешения показываем с объяснением, зачем они нужны. Управление — в системных настройках; из приложения открываем нужную страницу настроек. Без геолокации всё работает, просто без блока «Популярно рядом».',
SCR('perms',`${NAV('Разрешения',{back:'settings'})}<div class="body">
<div class="grp-t" style="margin-top:4px">Приложение</div><div class="grp">${LI('locate','Геолокация','Только в приложении')}${LI('camera','Камера','Никогда')}${LI('image','Фото','Все')}${LI('bell','Уведомления','Разрешены')}</div>
<div class="grp-t">Зачем это нужно</div>
<div style="background:var(--card);border-radius:20px;padding:14px 16px;display:flex;gap:12px;align-items:flex-start"><span style="color:var(--pink)">${ic('locate','style="width:19px;height:19px"')}</span><span class="t-cap" style="line-height:1.45">Геолокацию используем только для сортировки по расстоянию. Без неё всё работает — просто без блока «Популярно рядом».</span></div>
<div class="t-cap" style="margin-top:12px;padding:0 4px;line-height:1.4">Изменить разрешение можно в системных настройках — откроем нужную страницу.</div>
</div>${HI}`));

add('delacc','user','13 · Удаление аккаунта','Повторное подтверждение паролем или Google. Аккаунт удаляется через 30 дней: профиль, планы, сохранённое, друзья и переписки. Войти снова в течение 30 дней — и удаление отменяется.',
SCR('delacc',`${NAV('Удалить аккаунт',{back:'settings'})}<div class="body">
<div style="width:60px;height:60px;border-radius:20px;background:var(--card);display:flex;align-items:center;justify-content:center;margin-top:8px;color:var(--pink)">${ic('trash','style="width:28px;height:28px"')}</div>
<div class="h-big" style="margin-top:22px">Удалить аккаунт?</div><div class="sub">Через 30 дней удалим профиль, планы, сохранённое, друзей и переписки. Отменить можно в любой момент — просто войди снова.</div>
${FIELD('Пароль','••••••••••',{icon:'lock',st:'focus',right:ic('eye')})}
<div class="t-cap" style="margin-top:10px;padding:0 4px;line-height:1.4">Вход через Google — подтверждение окном Google вместо пароля.</div>
</div><div class="cta"><button class="btn pri" data-go="delaccOk">Удалить аккаунт</button></div>${HI}`));

add('delaccOk','user','13 · Запрос на удаление','Состояние после подтверждения: аккаунт помечен к удалению, выход выполнен. Вход в течение 30 дней отменяет удаление и возвращает всё как было.',
SCR('delaccOk',`<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;padding:0 32px;text-align:center"><div style="width:84px;height:84px;border-radius:28px;background:var(--card);display:flex;align-items:center;justify-content:center;color:var(--pink)">${ic('clock','style="width:40px;height:40px"')}</div>
<div class="h-big" style="margin-top:28px">Аккаунт будет удалён через 30 дней</div><div class="sub">Войди снова в любой момент — и удаление отменится. Письмо с подтверждением отправлено на почту.</div></div>
<div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn sec" data-go="settings">Отменить удаление</button><button class="btn ghost" data-go="welcome">Выйти</button></div>${HI}`));

add('sort','user','10 · Сохранённое · Сортировка','Шторка поверх сохранённого: один вариант сортировки, плюс переключатель «Скрыть прошедшие». Выбор применяется сразу, «Архив» — отдельный чип на самом экране.',
SCR('sort',`<img src="out/raw2/saved.png" style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;object-position:top"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div class="t-h3" style="text-align:center;margin-bottom:12px">Сортировка</div>
<div class="grp">${[['По дате','ближайшие сначала',1],['По цене','сначала недорогие',0],['По категории','по жанрам',0]].map(([t,s,o])=>`<div class="opt" style="height:64px"><div style="flex:1"><div style="font-size:15px">${t}</div><div class="sub2">${s}</div></div><div class="radio ${o?'on':''}"></div></div>`).join('')}</div>
<div class="grp" style="margin-top:14px">${LI('calendar','Скрыть прошедшие','',{tg:true})}</div>
<div style="display:flex;gap:10px;margin-top:16px"><button class="btn sec" style="flex:1" data-go="saved">Сбросить</button><button class="btn pri" style="flex:1.6" data-go="saved">Показать · 6</button></div></div>`));

add('sw','biz','14 · Переключение Личный ↔ Бизнес','Шторка открывается тапом по @username в профиле или пункту «ВРЫВ для бизнеса». Один аккаунт — два режима: личный профиль и заведение. Бизнес-кабинет запоминается: следующий запуск откроется в последнем режиме. В MVP — одно заведение на владельца.',
SCR('sw',`<img src="out/raw2/prof.png" style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;object-position:top"><div class="dim"></div>
<div class="bsheet"><div class="grab"></div><div class="t-h3" style="text-align:center;margin-bottom:14px">Переключить профиль</div>
<div class="grp" style="background:#22222A"><div class="li noi" style="min-height:68px" data-go="prof">${AV('av2',44)}<div><div style="font-size:15.5px">Максим</div><div class="t-cap" style="font-size:12.5px">Личный профиль</div></div><span class="v" style="color:var(--pink)">${ic('check','style="width:20px;height:20px;stroke-width:2.4"')}</span></div>
<div class="li noi" style="min-height:68px" data-go="d1"><div class="logo-v" style="background:var(--pink);color:#fff">13</div><div><div style="font-size:15.5px">Loft 13</div><div class="t-cap" style="font-size:12.5px">Бар · Владелец</div></div><span class="v"><span class="dot"></span></span></div>
<div class="t-cap" style="text-align:center;padding:12px 4px 0;line-height:1.4">В MVP — одно заведение на владельца. Второе заведение и команда — после MVP.</div></div>`));
