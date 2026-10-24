/* ===== БИЗНЕС ===== */
add('biz','biz','15 · ВРЫВ для бизнеса · выбор типа','Вход в бизнес из личного профиля. Отдельная регистрация не нужна: бизнес-роль привязывается к текущему аккаунту. «Добавить заведение» — новая площадка, «Я уже управляю» — поиск существующей карточки и заявка на управление (без дублей). В MVP — одно заведение на владельца.',
SCR('biz',`${NAV('',{back:'prof',close:true})}<div class="body">
<div style="display:flex;align-items:center;gap:10px;margin-top:4px"><img src="ph/logo.png" style="height:38px"><span class="tag lime">для бизнеса</span></div>
<div class="h-big" style="margin-top:22px;font-size:32px">Создавай события. Собирай людей. Развивай своё место.</div>
<div style="margin-top:28px">${[['store','Добавить заведение','Бар, клуб, кафе, площадка, арт-пространство','v1'],['search','Я уже управляю заведением','Найдём карточку и подтвердим права','claim'],].map(([i,t,s,go],k)=>`<div class="opt" style="height:auto;padding:16px;align-items:flex-start;${k===0?'box-shadow:inset 0 0 0 1.5px var(--pink)':''}"${g(go)}><div class="oico" style="${k===0?'background:var(--pink);color:#fff':''}">${ic(i)}</div><div style="flex:1"><div style="font-size:16px">${t}</div><div class="sub2" style="line-height:1.35;margin-top:4px">${s}</div></div><span style="color:var(--text2);margin-top:8px">${ic('chevR','style="width:18px;height:18px"')}</span></div>`).join('')}</div>
<div style="display:flex;gap:10px;align-items:flex-start;margin-top:10px;padding:0 4px"><span style="color:var(--text2)">${ic('user','style="width:17px;height:17px"')}</span><span class="t-cap" style="line-height:1.4">Используем аккаунт <b style="color:var(--text)">@max.vryv</b>. Личный профиль останется приватным — гости увидят только страницу заведения.</span></div>
</div>${HI}`));

add('claim','biz','15 · Заявка на существующее заведение','Поиск по названию или адресу среди уже созданных карточек (в т.ч. добавленных командой ВРЫВ). Если площадка есть — «Это моё» отправляет заявку на управление, дубль не создаётся. У одного заведения может быть несколько управляющих: заявка уходит текущему владельцу и модерации.',
SCR('claim',`${NAV('Найти заведение',{back:'biz'})}<div class="body">
<div class="inp focus">${ic('search')}<span class="sp">Prime<span class="caret"></span></span>${ic('x','style="width:18px;height:18px"')}</div>
<div class="grp-t" style="margin-top:18px">Уже в ВРЫВ</div>
<div class="grp">${[['PH','Prime Hall','пр. Победителей, 65 · Концертная площадка','Есть управляющий'],['PR','Prime Bar','ул. Зыбицкая, 6 · Бар','Без управляющего']].map(([l,t,s,st],i)=>`<div class="li noi" style="min-height:78px"><div class="logo-v" style="${i?'background:#26262E;color:var(--text)':''}">${l}</div><div style="flex:1;min-width:0"><div style="font-size:15.5px">${t}</div><div class="t-cap" style="font-size:12.5px;margin-top:2px">${s}</div><div style="margin-top:6px"><span class="status ${i?'st-ok':'st-mute'}" style="height:20px;font-size:11px">${st}</span></div></div><button class="btn ${i?'pri':'sec'} sm" style="height:34px;padding:0 14px"${i?g('v4'):''}>Это моё</button></div>`).join('')}</div>
<div style="background:var(--card);border-radius:20px;padding:16px;margin-top:4px;display:flex;gap:12px;align-items:center" data-go="v1"><div class="oico">${ic('plus')}</div><div style="flex:1"><div class="t-h3" style="font-size:15px">Нет в списке?</div><div class="t-cap" style="margin-top:2px">Добавь новое заведение</div></div>${ic('chevR','style="color:var(--text2)"')}</div>
</div>${HI}`));

const VPROG=(n,back)=>`${PROG(n,4,back)}`;
const CATS=['Бар','Ночной клуб','Кафе','Ресторан','Концертная площадка','Арт-пространство','Другое'];
add('v1','biz','16 · Регистрация заведения · 1 из 4','Основное: название, категория, город, адрес с автодополнением и точкой на карте (пин можно сдвинуть), описание до 300 символов, главное фото/логотип. Поля сохраняются как черновик после каждого шага — можно выйти и вернуться.',
`<div class="screen long" id="v1">${STATUS}${VPROG(1,'biz')}<div style="padding:106px 16px 120px">
<div class="h-big" style="margin-top:6px;font-size:28px">О заведении</div>
<div style="display:flex;gap:12px;margin-top:18px;align-items:center"><div class="upl" style="width:84px;height:84px;border-radius:24px;flex:none">${ic('camera')}</div><div><div class="t-h3" style="font-size:15px">Логотип или главное фото</div><div class="t-cap" style="margin-top:3px">Квадрат от 800 px</div></div></div>
${FIELD('Название','Loft 13',{st:'focus'})}
<div class="fl"><label>Категория</label><div style="display:flex;flex-wrap:wrap;gap:7px">${CATS.map(c=>`<div class="chip ${c==='Бар'?'sel':''}" style="height:34px">${c}</div>`).join('')}</div></div>
${FIELD('Город','Минск',{icon:'pin',right:ic('chevD')})}
${FIELD('Адрес','ул. Немига, 13',{right:'<span class="link" style="font-size:13px">На карте</span>'})}
<div style="height:120px;border-radius:18px;margin-top:10px;background:url(map/minsk_dark.png) -380px -470px/1024px 1536px;position:relative"><div style="position:absolute;left:50%;top:50%;transform:translate(-50%,-100%);color:var(--pink)">${icf('pin','#FF1D8E').replace('class="ico"','class="ico" style="width:34px;height:34px;fill:#FF1D8E;stroke:#0B0B0F;stroke-width:1.4"')}</div><span class="tag glass" style="position:absolute;left:10px;bottom:10px">Сдвинь пин, если нужно</span></div>
${FIELD('Описание','Бар с винилом и диджеями по пятницам. Камерный танцпол на 120 человек.',{area:true})}
</div><div class="cta-bar"><button class="btn pri" data-go="v2">Далее</button></div></div>`,{long:true});

add('v2','biz','16 · Регистрация заведения · 2 из 4','Дополнительно: галерея (до 12 фото), часы по дням с быстрым «как в пн», музыка и особенности — те же чипы, что в интересах пользователей (это теги для рекомендаций), ценовой диапазон, возраст, вместимость (необязательно), доступность.',
`<div class="screen long" id="v2">${STATUS}${VPROG(2,'v1')}<div style="padding:106px 16px 120px">
<div class="h-big" style="margin-top:6px;font-size:28px">Атмосфера и детали</div>
<div class="fl"><label>Фотографии · 3 из 12</label><div style="display:grid;grid-template-columns:repeat(4,1fr);gap:8px">${['wine','jazz','concert'].map(p=>`<div class="ph" style="aspect-ratio:1;border-radius:14px"><img src="${I(p)}"></div>`).join('')}<div class="upl" style="aspect-ratio:1;border-radius:14px">${ic('plus')}</div></div></div>
<div class="fl"><label>Часы работы</label><div class="grp" style="margin:0">${[['Пн–Чт','18:00 – 02:00'],['Пт–Сб','18:00 – 06:00'],['Вс','Выходной']].map(([d,t])=>`<div class="li noi"><span>${d}</span><span class="v">${t}${ic('chevR')}</span></div>`).join('')}</div></div>
<div class="fl"><label>Музыка</label><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['Техно','Хаус','Диско','Винил','Джаз','Инди'],['Хаус','Диско','Винил'])}</div></div>
<div class="fl"><label>Особенности</label><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['Живая музыка','Танцпол','Коктейли','Тихая атмосфера','Подходит для знакомств','Терраса'],['Танцпол','Коктейли','Подходит для знакомств'])}</div></div>
<div class="fl"><label>Средний чек</label><div style="display:flex;background:var(--card);border-radius:14px;padding:3px">${['до 20','20–50','50–100','100+'].map((t,i)=>`<div style="flex:1;height:36px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===1?'background:#2E2E37':'color:var(--text2)'}">${t}</div>`).join('')}</div></div>
<div class="g2">${FIELD('Возраст','18+',{right:ic('chevD')})}${FIELD('Вместимость','120',{hint:'Необязательно'})}</div>
<div class="grp" style="margin-top:14px">${LI('access','Доступно для маломобильных','',{tg:true})}</div>
</div><div class="cta-bar"><button class="btn pri" data-go="v3">Далее</button></div></div>`,{long:true});

add('v3','biz','16 · Регистрация заведения · 3 из 4','Контакты для гостей и для связи с ВРЫВ. Telegram — только ссылка для связи, не способ входа. Юридические данные (наименование, УНП, контакт для юр. вопросов) — в закрытом блоке с замком: не публикуются в карточке.',
`<div class="screen long" id="v3">${STATUS}${VPROG(3,'v2')}<div style="padding:106px 16px 120px">
<div class="h-big" style="margin-top:6px;font-size:28px">Контакты</div>
${FIELD('Контактное лицо','Максим Ковалёв')}
${FIELD('Служебный Email','hello@loft13.by',{icon:'mail'})}
${FIELD('Телефон','+375 29 123-45-67',{icon:'phone'})}
${FIELD('Telegram заведения','@loft13bar',{icon:'send',hint:'Необязательно · только как контакт'})}
${FIELD('Instagram','@loft13.minsk',{icon:'camera'})}
${FIELD('Сайт','loft13.by',{icon:'globe'})}
<div style="background:var(--card);border-radius:22px;padding:16px;margin-top:20px">
<div style="display:flex;gap:10px;align-items:center"><span style="color:var(--lime)">${ic('lock','style="width:19px;height:19px"')}</span><span class="t-h3" style="font-size:15px">Юридические данные</span><span class="demo" style="margin-left:auto">не публикуются</span></div>
<div class="t-cap" style="margin-top:6px;line-height:1.4">Нужны для проверки и документов. Гости их не увидят.</div>
<div class="inp" style="margin-top:12px;background:#24242C"><span class="sp">ООО «Лофт Тринадцать»</span></div>
<div class="inp" style="margin-top:8px;background:#24242C"><span class="sp ph-t">УНП · 9 цифр</span></div>
<div class="inp" style="margin-top:8px;background:#24242C"><span class="sp ph-t">Email для юридических вопросов</span></div></div>
</div><div class="cta-bar"><button class="btn pri" data-go="v4">Далее</button></div></div>`,{long:true});

add('v4','biz','17 · Подтверждение права управления · 4 из 4','Способы по возрастанию усилий. Почта на домене заведения — код за минуту. Официальный аккаунт — разовое сообщение/сторис с кодом. Документы — только если первые два невозможны (свидетельство, доверенность). Ручная модерация — звонок команды ВРЫВ. Срок проверки виден заранее.',
SCR('v4',`${VPROG(4,'v3')}<div class="body"><div class="h-big" style="margin-top:6px;font-size:28px">Подтверди, что ты управляешь Loft 13</div>
<div style="margin-top:20px">${[['mail','Служебная почта','Код на hello@loft13.by · ~1 мин',1],['camera','Официальный аккаунт','Код в Instagram @loft13.minsk · до 1 дня',0],['shield','Документы организации','Если нет доступа к почте · 1–2 дня',0],['phone','Ручная проверка','Позвоним по номеру заведения · 1–3 дня',0]].map(([i,t,s,o])=>`<div class="opt ${o?'on':''}" style="height:72px"><div class="oico" style="${o?'color:var(--pink)':''}">${ic(i)}</div><div style="flex:1"><div style="font-size:15.5px">${t}</div><div class="sub2">${s}</div></div><div class="radio ${o?'on':''}"></div></div>`).join('')}</div>
<div class="t-cap" style="padding:0 4px;line-height:1.4;margin-top:6px">После подтверждения можно сразу публиковать события. Первые 3 события проходят быструю модерацию.</div>
</div><div class="cta"><button class="btn pri" data-go="vstat">Отправить заявку</button></div>${HI}`));

add('vstat','biz','17 · Статус заявки','Статусы: Черновик → На проверке → (Нужны уточнения) → Подтверждено / Отклонено. Статус дублируется пушем и письмом. «Нужны уточнения» открывает конкретное поле с комментарием модератора. Пока идёт проверка, можно готовить черновики событий.',
SCR('vstat',`${NAV('Заявка',{back:'biz',close:true})}<div class="body">
<div style="display:flex;gap:14px;align-items:center;margin-top:4px"><div class="logo-v" style="background:var(--pink);color:#fff;width:60px;height:60px;border-radius:18px;font-size:22px">13</div><div><div class="t-title" style="font-size:24px">Loft 13</div><div class="t-cap" style="margin-top:3px">Бар · ул. Немига, 13</div></div></div>
<div style="background:var(--card);border-radius:24px;padding:18px;margin-top:20px"><span class="status st-wait"><i style="background:var(--lime)"></i>На проверке</span><div class="t-h2" style="margin-top:12px;font-size:20px">Заявка на проверке</div><div class="sub" style="margin-top:6px">Обычно до 24 часов. Сообщим пушем и на hello@loft13.by.</div></div>
<div style="margin-top:22px;padding:0 6px">${[['Черновик','22 окт, 12:04','done'],['На проверке','22 окт, 12:10 · почта подтверждена','now'],['Нужны уточнения','если модератору что-то неясно','opt'],['Подтверждено','откроется бизнес-кабинет','next']].map(([t,s,st],i,a)=>`<div style="display:flex;gap:14px"><div style="display:flex;flex-direction:column;align-items:center"><div style="width:22px;height:22px;border-radius:50%;flex:none;display:flex;align-items:center;justify-content:center;${st==='done'?'background:var(--lime);color:#0B0B0F':st==='now'?'box-shadow:inset 0 0 0 6px var(--pink)':'box-shadow:inset 0 0 0 1.8px #4A4A55'}">${st==='done'?ic('check','style="width:13px;height:13px;stroke-width:3"'):''}</div>${i<a.length-1?'<div style="width:2px;flex:1;background:#2A2A33;margin:4px 0"></div>':''}</div><div style="padding-bottom:20px"><div style="font-size:15px;font-weight:600;${st==='opt'||st==='next'?'color:var(--text2)':''}">${t}</div><div class="t-cap" style="margin-top:2px">${s}</div></div></div>`).join('')}</div>
<div style="display:flex;gap:6px;flex-wrap:wrap;padding:0 4px"><span class="status st-mute">Черновик</span><span class="status st-wait"><i style="background:var(--lime)"></i>На проверке</span><span class="status st-pink"><i></i>Нужны уточнения</span><span class="status st-ok"><i></i>Подтверждено</span><span class="status st-mute" style="text-decoration:line-through">Отклонено</span></div>
</div><div class="cta"><button class="btn sec" data-go="d1">Подготовить черновик события</button></div>${HI}`));

/* ===== КАБИНЕТ ===== */
const KPI=(icon,l,n,d,neg)=>`<div class="kpi"><div class="l">${ic(icon)}${l}</div><div class="n">${n}</div><div class="dl ${neg?'neg':''}">${d}</div></div>`;
add('d1','biz','18 · Бизнес-кабинет · Обзор','Шапка с заведением, статусом и переключением площадок (шторка). Период: Сегодня / 7 / 30 дней. 6 компактных метрик без диаграмм; динамика лаймом. Дальше — ближайшее событие, черновики и важные уведомления. Создание события — на вкладке «События» (плюс в шапке), обзор остаётся чистым. Навигация — 5 вкладок, активная лаймовая: так бизнес-режим отличается от личного.',
`<div class="screen long" id="d1">${STATUS}<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between;align-items:center">${BIZHEAD()}<div class="ibtn" data-go="sw">${ic('swap','style="width:19px;height:19px"')}</div></div>
<div style="padding:124px 16px 0">
<div style="display:flex;background:var(--card);border-radius:14px;padding:3px">${['Сегодня','7 дней','30 дней'].map((t,i)=>`<div style="flex:1;height:34px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===1?'background:#2E2E37':'color:var(--text2)'}">${t}</div>`).join('')}</div>
<div style="display:flex;justify-content:space-between;align-items:center;margin:16px 4px 10px"><span class="t-h2" style="font-size:19px">Неделя</span><span class="demo">пример данных</span></div>
<div class="kpis">${KPI('eye','Просмотры профиля','3 482','+18%')}${KPI('cards','Просмотры событий','9 120','+24%')}${KPI('bookmark','Сохранения','412','+9%')}${KPI('check','Нажали «Иду»','268','+31%')}${KPI('ticket','Переходы к билетам','154','+12%')}${KPI('users','Новые подписчики','87','−4%',1)}</div>
<div class="sec-h" style="margin:24px 0 10px;padding:0 4px"><span class="t-h2" style="font-size:19px">Ближайшее</span><a data-go="d2">Все события</a></div>
<div style="background:var(--card);border-radius:24px;padding:10px;display:flex;gap:12px;align-items:center" data-go="d4"><div class="ph" style="width:76px;height:96px;border-radius:16px;flex:none"><img src="${I('dj')}"></div><div style="flex:1;min-width:0"><span class="status st-ok" style="height:20px;font-size:11px"><i></i>Опубликовано</span><div class="t-h3" style="font-size:15.5px;margin-top:6px">Katrin Velvet b2b Nox</div><div class="t-cap" style="margin-top:2px">Завтра · 22:00 · 35 BYN</div><div style="display:flex;gap:12px;margin-top:6px;font-size:12.5px;font-weight:600"><span>${ic('check','style="width:13px;height:13px;display:inline;vertical-align:-2px"')} 128 «Иду»</span><span class="muted">2,1 тыс. просмотров</span></div></div></div>
<div class="sec-h" style="margin:24px 0 10px;padding:0 4px"><span class="t-h2" style="font-size:19px">Важное</span></div>
<div class="grp">${LI('alert','Черновик «Disco Friday» не завершён','',{go:'e1'})}${LI('chat','3 вопроса по событию','',{go:'d5'})}${LI('bell','Модерация: «Vinyl Sunday» опубликовано','',{go:'d2'})}</div>
</div>${BTAB(0)}</div>`,{long:true});


const EV=(p,t,d,pr,st,stc,n,go='d4')=>`<div style="background:var(--card);border-radius:24px;padding:10px;margin-bottom:10px"${g(go)}><div style="display:flex;gap:12px"><div class="ph" style="width:72px;height:92px;border-radius:16px;flex:none"><img src="${I(p)}"></div><div style="flex:1;min-width:0;padding-top:2px"><span class="status ${stc}" style="height:20px;font-size:11px"><i></i>${st}</span><div class="t-h3" style="font-size:15.5px;margin-top:6px">${t}</div><div class="t-cap" style="margin-top:2px">${d} · ${pr}</div><div style="font-size:12.5px;font-weight:600;margin-top:5px">${n}</div></div></div>
<div style="display:flex;gap:6px;margin-top:10px;border-top:1px solid var(--line);padding-top:10px">${[['edit','Изменить'],['copy','Дублировать'],['chart','Статистика'],['more','']].map(([i,l])=>`<div style="flex:${l?1:'none'};height:34px;${l?'':'width:40px;'}border-radius:12px;background:#24242C;display:flex;align-items:center;justify-content:center;gap:6px;font-size:12.5px;font-weight:600;color:var(--text)">${ic(i,'style="width:15px;height:15px"')}${l}</div>`).join('')}</div></div>`;
add('d2','biz','19 · Управление мероприятиями','Вкладки-чипы со счётчиками: Активные, Запланированные, Черновики, Завершённые, Отменённые. Карточка: афиша, название, дата/время, цена, статус, число заинтересованных, быстрые действия. «Ещё» — отменить (с уведомлением тех, кто нажал «Иду»), скрыть, удалить черновик.',
SCR('d2',`<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between;align-items:center"><div class="t-title">События</div><div class="ibtn" style="background:var(--pink)" data-go="e1">${ic('plus','style="stroke-width:2.4"')}</div></div>
<div style="position:absolute;top:112px;left:0;right:0"><div class="chips">${[['Активные',3,1],['Запланированные',2],['Черновики',1],['Завершённые',24],['Отменённые',1]].map(([t,n,o])=>`<div class="chip ${o?'on':''}">${t} <span style="opacity:.6">${n}</span></div>`).join('')}</div></div>
<div class="body" style="top:164px">
${EV('dj','Katrin Velvet b2b Nox','Пт 23 окт · 22:00','35 BYN','Опубликовано','st-ok','128 «Иду» · 412 сохранений')}
${EV('concert','Warehouse Session','Сб 24 окт · 23:00','от 25 BYN','На модерации','st-wait','Появится после проверки')}
${EV('jazz','Vinyl Sunday','Вс 25 окт · 19:00','Бесплатно','Нужны правки','st-pink','Замените афишу: мелкий текст')}
</div><div class="fade-bottom"></div>${BTAB(1)}`));

add('e1','biz','20 · Создание мероприятия · 1 из 3','Шаг «Что»: афиша (4:5, предпросмотр кадрирования), название, описание, категория и жанры. Жанры = теги рекомендаций: именно по ним событие попадёт в «Потому что тебе нравится…». Автосохранение черновика.',
`<div class="screen long" id="e1">${STATUS}${NAV('Новое событие',{back:'d2',right:'<span class="lnk">Черновик</span>'})}<div style="padding:106px 16px 120px">
<div style="display:flex;gap:6px;margin:4px 0 16px">${[1,0,0].map(o=>`<i style="flex:1;height:4px;border-radius:2px;background:${o?'var(--pink)':'#2A2A33'};display:block"></i>`).join('')}</div>
<div class="ph" style="height:260px;border-radius:24px"><img src="${I('concert')}"><div class="in" style="right:12px;bottom:12px;display:flex;gap:8px"><div class="ibtn glass">${ic('image','style="width:19px;height:19px"')}</div><div class="ibtn glass">${ic('edit','style="width:18px;height:18px"')}</div></div><div class="in" style="left:12px;top:12px"><span class="tag glass">Афиша · 4:5</span></div></div>
${FIELD('Название','Warehouse Session',{})}
${FIELD('Описание','Ночь индустриального техно: три сета, свет от Lumen Lab, бар до закрытия.',{area:true})}
<div class="fl"><label>Категория</label><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['Вечеринка','Концерт','Стендап','Выставка','Лекция','Квиз'],['Вечеринка'])}</div></div>
<div class="fl"><label>Жанры и теги · влияют на рекомендации</label><div style="display:flex;flex-wrap:wrap;gap:7px">${chipsOf(['Техно','Андеграунд','Хаус','Индастриал','Ночь','Новые знакомства'],['Техно','Андеграунд','Индастриал'])}</div></div>
</div><div class="cta-bar"><button class="btn pri" data-go="e2">Далее · когда и где</button></div></div>`,{long:true});

add('e2','biz','20 · Создание мероприятия · 2 из 3','Шаг «Когда, где, условия». Место по умолчанию — адрес заведения (для организатора — выбирается каждый раз). Цена или «Бесплатно», ссылка на билеты/регистрацию с проверкой URL, возраст, лимит мест, разрешение «Поиск компании». Ошибки валидации показываются у поля: на примере — время окончания раньше начала.',
`<div class="screen long" id="e2">${STATUS}${NAV('Новое событие',{back:'e1',right:'<span class="lnk">Черновик</span>'})}<div style="padding:106px 16px 120px">
<div style="display:flex;gap:6px;margin:4px 0 6px">${[1,1,0].map(o=>`<i style="flex:1;height:4px;border-radius:2px;background:${o?'var(--pink)':'#2A2A33'};display:block"></i>`).join('')}</div>
<div class="g2">${FIELD('Начало','Сб 24 · 23:00',{icon:'calendar'})}${FIELD('Окончание','Сб 24 · 06:00',{st:'err',hint:'Раньше начала. Может, Вс 25?',hst:'err'})}</div>
${FIELD('Место','Loft 13 · ул. Немига, 13',{icon:'pin',right:ic('chevR')})}
<div class="fl"><label>Вход</label><div style="display:flex;background:var(--card);border-radius:14px;padding:3px">${['Бесплатно','Платно'].map((t,i)=>`<div style="flex:1;height:38px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===1?'background:#2E2E37':'color:var(--text2)'}">${t}</div>`).join('')}</div></div>
<div class="g2">${FIELD('Цена от, BYN','25')}${FIELD('Цена до, BYN','40')}</div>
${FIELD('Ссылка на билеты','ticketpro.by/warehouse-24',{icon:'link',st:'ok'})}
<div class="g2">${FIELD('Возраст','18+',{right:ic('chevD')})}${FIELD('Лимит мест','Без лимита',{ph:true})}</div>
<div class="grp" style="margin-top:16px">${LI('users','Разрешить «Ищу компанию»','',{tg:true})}${LI('bell','Напомнить гостям за 3 часа','',{tg:true})}</div>
</div><div class="cta-bar"><button class="btn pri" data-go="e3">Предпросмотр</button></div></div>`,{long:true});

add('e3','biz','20 · Предпросмотр и публикация · 3 из 3','Событие показано ровно так, как его увидят пользователи: карточка в ленте, в свайпах и на карте (переключатель вида). Чек-лист перед публикацией. «Опубликовать» отправляет на модерацию (автопроверка + выборочно человек). Подтверждённые заведения с хорошей историей публикуются сразу.',
SCR('e3',`${NAV('Предпросмотр',{back:'e2'})}<div class="body">
<div style="display:flex;background:var(--card);border-radius:14px;padding:3px">${['Лента','Свайпы','Карта'].map((t,i)=>`<div style="flex:1;height:34px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===0?'background:#2E2E37':'color:var(--text2)'}">${t}</div>`).join('')}</div>
<div class="ph scrim" style="margin-top:14px;height:300px;border-radius:28px"><img src="${I('concert')}"><div class="in" style="top:14px;left:14px;right:14px;display:flex;justify-content:space-between;align-items:center"><span class="tag glass">Сб · 23:00</span><div class="ibtn glass">${ic('heart')}</div></div><div class="in" style="left:18px;right:18px;bottom:18px"><span class="tag lime">Теги: Техно · Хаус</span><div class="t-title" style="font-size:27px;margin-top:10px">Warehouse Session</div><div class="t-cap" style="color:rgba(245,245,247,.75);margin-top:6px">Loft 13 · Немига, 13 · от 25 BYN</div></div></div>
<div class="grp" style="margin-top:14px">${[['Афиша 4:5, текст читается',1],['Время и место указаны',1],['Ссылка на билеты работает',1],['Возраст 18+ отмечен',1]].map(([t])=>`<div class="li noi" style="min-height:44px;font-size:14.5px"><span style="color:var(--lime)">${ic('check','style="width:18px;height:18px;stroke-width:2.4"')}</span><span>${t}</span></div>`).join('')}</div>
</div><div class="cta-bar" style="display:flex;gap:10px"><button class="btn sec" style="flex:1" data-go="d2">В черновик</button><button class="btn pri" style="flex:1.3" data-go="e4">Опубликовать</button></div>`));

add('e4','biz','20 · Отправлено на модерацию','Успешная отправка: статус «На модерации», ожидаемое время, что будет дальше. Пуш придёт при публикации или если нужны правки.',
SCR('e4',`<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;padding:0 32px;text-align:center"><div class="ph" style="width:150px;height:188px;border-radius:24px;transform:rotate(-4deg);box-shadow:0 20px 40px rgba(0,0,0,.5)"><img src="${I('concert')}"></div>
<div style="margin-top:30px"><span class="status st-wait"><i style="background:var(--lime)"></i>На модерации</span></div><div class="h-big" style="margin-top:14px">Событие отправлено</div><div class="sub">Обычно проверка занимает до 2 часов. Как только «Warehouse Session» появится в афише, пришлём уведомление.</div></div>
<div class="cta" style="display:flex;flex-direction:column;gap:10px"><button class="btn pri" data-go="d2">К событиям</button><button class="btn ghost" data-go="e1">Создать ещё одно</button></div>${HI}`));

const spark=(pts,c,w=330,h=110)=>{const mx=Math.max(...pts),mn=Math.min(...pts);const p=pts.map((v,i)=>`${(i/(pts.length-1)*w).toFixed(1)},${(h-8-(v-mn)/(mx-mn)*(h-20)).toFixed(1)}`).join(' ');return `<svg width="${w}" height="${h}" viewBox="0 0 ${w} ${h}" style="display:block"><polyline points="${p}" fill="none" stroke="${c}" stroke-width="2.4" stroke-linejoin="round" stroke-linecap="round"/></svg>`};
add('d4','biz','21 · Аудитория и аналитика','Только агрегаты: никаких имён, истории свайпов и точной геолокации. Воронка Просмотры → Сохранения → «Иду» → Билеты; подпись прямо говорит, что «Иду» — намерение, а не посещаемость. Для MVP аналитика упрощена: просмотры, сохранения, «Иду» и переходы к билетам, плюс простой список популярных событий. Срез по событию открывается из карточки события (сценарий Е).',
`<div class="screen long" id="d4">${STATUS}<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between;align-items:center"><div class="t-title">Аудитория</div><span class="demo">пример данных</span></div>
<div style="padding:112px 16px 0">
<div class="chips" style="padding:0"><div class="chip on">Все события</div><div class="chip">Katrin Velvet</div><div class="chip">Warehouse</div></div>
<div style="display:flex;background:var(--card);border-radius:14px;padding:3px;margin-top:12px">${['Сегодня','7 дней','30 дней'].map((t,i)=>`<div style="flex:1;height:34px;border-radius:11px;display:flex;align-items:center;justify-content:center;font-size:14px;font-weight:600;${i===2?'background:#2E2E37':'color:var(--text2)'}">${t}</div>`).join('')}</div>
<div style="background:var(--card);border-radius:24px;padding:16px;margin-top:12px"><div class="t-h3" style="font-size:15px">Воронка</div>
${[['Просмотры событий','31 840',100],['Сохранения','2 214',42],['Нажали «Иду»','1 186',28],['Переходы к билетам','604',16]].map(([t,n,w])=>`<div style="margin-top:12px"><div style="display:flex;justify-content:space-between;font-size:13.5px;font-weight:600"><span>${t}</span><span>${n}</span></div><div style="height:8px;border-radius:4px;background:#2A2A33;margin-top:6px"><div style="height:8px;border-radius:4px;background:var(--pink);width:${w}%"></div></div></div>`).join('')}
<div style="display:flex;gap:8px;margin-top:14px;align-items:flex-start" class="t-cap"><span>${ic('alert','style="width:15px;height:15px"')}</span><span style="line-height:1.4">«Иду» — намерение посетить, а не фактическая посещаемость.</span></div></div>
<div class="sec-h" style="margin:22px 0 8px;padding:0 4px"><span class="t-h2" style="font-size:19px">Популярные события</span></div>
<div class="grp">${[['dj','Katrin Velvet b2b Nox','9,1 тыс. · 412 сохр.','+31%'],['techno','Subsonic Night','6,4 тыс. · 280 сохр.','+12%'],['jazz','Vinyl Sunday','3,2 тыс. · 140 сохр.','+5%']].map(([p,t,s,d],i)=>`<div class="li noi" style="min-height:66px"><span class="muted" style="width:14px;font-weight:700">${i+1}</span><div class="ph" style="width:44px;height:44px;border-radius:11px;flex:none"><img src="${I(p)}"></div><div style="min-width:0"><div style="font-size:14.5px">${t}</div><div class="t-cap" style="font-size:12px">${s}</div></div><span class="v dl">${d}</span></div>`).join('')}</div>
${BTAB(2)}</div>`,{long:true});

add('d5','biz','22 · Сообщения','Входящие обращения, вопросы по событиям, обсуждения, где заведение участвует. Диалог начинает только пользователь (или он подписался и разрешил сообщения). Массовые рассылки недоступны: вместо них — «Новость в профиле заведения» для подписчиков в ленте.',
SCR('d5',`<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between;align-items:center"><div class="t-title">Сообщения</div><div class="ibtn">${ic('search','style="width:20px;height:20px"')}</div></div>
<div style="position:absolute;top:112px;left:0;right:0"><div class="chips"><div class="chip on">Все <span style="opacity:.7">3</span></div><div class="chip">По событиям</div><div class="chip">Обсуждения</div><div class="chip">Архив</div></div></div>
<div class="body" style="top:162px">
${[['av1','Алина','Katrin Velvet','Можно прийти с 17-летним братом?','2 мин',1],['av4','Дима','Warehouse Session','Будет ли гардероб? И до скольки бар?','18 мин',1],['av3','Катя','Вопрос о заведении','Есть ли у вас столики для компании из 6?','1 ч',1],['av6','Артём','Vinyl Sunday','Спасибо, отличный вечер!','вчера',0],['av5','Вика','Обсуждение · Ищу компанию','Loft 13: «Да, вход до 01:00»','пн',0]].map(([a,n,ctx,m,t,u])=>`<div style="display:flex;gap:12px;align-items:center;padding:10px 0" data-go="chat">${AV(a,52)}<div style="flex:1;min-width:0"><div style="display:flex;justify-content:space-between"><span class="t-h3" style="font-size:15.5px">${n}</span><span class="t-cap" style="font-size:12px">${t}</span></div><div style="font-size:12px;font-weight:650;color:var(--text2);margin-top:2px">${ctx}</div><div style="display:flex;justify-content:space-between;align-items:center;margin-top:2px;gap:8px"><span style="font-size:14px;${u?'':'color:var(--text2)'};white-space:nowrap;overflow:hidden;text-overflow:ellipsis">${m}</span>${u?'<span class="dot" style="flex:none"></span>':''}</div></div></div>`).join('')}
<div style="display:flex;gap:10px;align-items:flex-start;margin-top:10px;background:var(--card);border-radius:18px;padding:12px 14px"><span style="color:var(--text2)">${ic('shield','style="width:18px;height:18px"')}</span><span class="t-cap" style="line-height:1.4">Пишут только пользователи, которые сами обратились. Рассылки всем недоступны — используйте новости в профиле.</span></div>
</div><div class="fade-bottom"></div>${BTAB(3)}`));

add('chat','biz','22 · Диалог','Диалог привязан к событию (карточка сверху). Быстрые ответы-шаблоны из настроек. Ответ от имени заведения подписывается именем сотрудника — видно внутри команды.',
SCR('chat',`${NAV('',{back:'d5'})}<div style="position:absolute;top:58px;left:56px;right:16px;display:flex;gap:10px;align-items:center">${AV('av1',38)}<div><div class="t-h3" style="font-size:15px">Алина</div><div class="t-cap" style="font-size:12px">@alina.wave</div></div></div>
<div class="body" style="top:114px">
<div style="background:var(--card);border-radius:18px;padding:8px;display:flex;gap:10px;align-items:center"><div class="ph" style="width:44px;height:44px;border-radius:11px;flex:none"><img src="${I('dj')}"></div><div><div style="font-size:14px;font-weight:600">Katrin Velvet b2b Nox</div><div class="t-cap" style="font-size:12px">Пт 23 окт · 22:00 · 18+</div></div></div>
<div class="t-cap" style="text-align:center;margin:18px 0 12px;font-size:12px">Сегодня</div>
<div class="bubble in">Привет! Можно прийти с 17-летним братом?</div>
<div class="bubble out">Привет, Алина! К сожалению, событие 18+, на входе проверяем документы. В воскресенье у нас Vinyl Sunday 16+ — будем рады вам обоим.</div>
<div style="text-align:right;font-size:11px;color:var(--text2);margin:-2px 4px 10px">Максим · Loft 13 · 12:41</div>
<div class="bubble in">Супер, спасибо! Сохранила</div>
</div>
<div style="position:absolute;left:0;right:0;bottom:0;padding:10px 16px 34px;background:var(--bg);border-top:1px solid var(--line)"><div style="display:flex;gap:8px;margin-bottom:10px;overflow:hidden">${['Да, ждём!','Вход до 01:00','Гардероб есть'].map(t=>`<span class="chip sm" style="height:30px">${t}</span>`).join('')}</div><div style="display:flex;gap:10px;align-items:center"><div class="inp" style="flex:1;height:44px;border-radius:22px"><span class="sp ph-t">Сообщение</span></div><div class="ibtn" style="background:var(--pink);width:44px;height:44px;border-radius:22px">${ic('send','style="width:19px;height:19px"')}</div></div></div>`));

add('d6','biz','23 · Настройки заведения','Три блока: профиль заведения (название, фото, адрес, график), безопасность (верификация, история) и дополнительно. Управление командой и несколькими заведениями — вне MVP. «Выйти из бизнес-режима» возвращает в личный профиль, не разлогинивая.',
`<div class="screen long" id="d6">${STATUS}<div style="position:absolute;top:58px;left:16px;right:16px">${BIZHEAD()}</div><div style="padding:124px 16px 0">
<div class="grp-t">Профиль заведения</div><div class="grp">${LI('edit','Название и описание','Loft 13')}${LI('image','Фото','7')}${LI('grid','Категории и особенности','Бар · 6')}${LI('pin','Адрес','Немига, 13',{go:'vaddr'})}${LI('clock','График работы','Пн–Вс',{go:'vtime'})}${LI('phone','Контакты и ссылки','4')}</div>
<div class="grp-t">Безопасность</div><div class="grp">${LI('shield','Верификация','<span class="status st-ok" style="height:22px"><i></i>Подтверждено</span>')}${LI('clock','История изменений','')}</div>
<div class="grp-t">Дополнительно</div><div class="grp">${LI('bell','Уведомления','',{go:'bnotif'})}${LI('chat','Поддержка ВРЫВ','')}${LI('trash','Архивировать заведение','',{danger:true,chev:false})}</div>
<div class="grp">${LI('logout','Выйти из бизнес-режима','',{danger:false,chev:false,go:'prof'})}</div>
</div>${BTAB(4)}</div>`,{long:true});

/* ===== НАСТРОЙКИ ЗАВЕДЕНИЯ · НОВЫЕ ЭКРАНЫ ===== */


add('vtime','biz','23 · График работы','Часы по дням недели с быстрым копированием «как в пн». Работа после полуночи показывается следующим днём: «до 06:00» в субботу = ночь субботы. Особые дни — праздники и переносы. Изменения видны гостям сразу.',
SCR('vtime',`${NAV('График работы',{back:'d6'})}<div class="body">
<div class="h-big" style="margin-top:6px;font-size:26px">Часы работы</div><div class="sub">Изменения увидят гости сразу после сохранения.</div>
<div class="grp" style="margin-top:16px">${[['Пн','18:00 – 02:00',0],['Вт','18:00 – 02:00',0],['Ср','18:00 – 02:00',0],['Чт','18:00 – 02:00',0],['Пт–Сб','18:00 – 06:00',1],['Вс','Выходной',0]].map(([d,t,e])=>`<div class="li noi" style="${e?'box-shadow:inset 0 0 0 1.5px var(--pink);border-radius:0':''}"><span style="width:52px">${d}</span><span class="v">${t}${e?'<span class="link" style="margin-left:10px">как в пн</span>':''}${ic('chevR')}</span></div>`).join('')}</div>
<div class="grp" style="margin-top:0">${LI('calendar','Особые дни','3 праздника')}</div>
<div class="t-cap" style="margin-top:4px;padding:0 4px;line-height:1.4">После 00:00 показываем следующий день: «Пт–Сб до 06:00» — это ночь субботы.</div>
</div><div class="cta"><button class="btn pri" data-go="d6">Сохранить</button></div>${HI}`));

add('vaddr','biz','23 · Адрес заведения','Адрес с автодополнением и пином на карте: пин можно сдвинуть — адрес обновится сам. В карточке заведения показывается адрес и район; вход и подъезд лучше описать словами.',
SCR('vaddr',`${NAV('Адрес',{back:'d6'})}<div class="body">
<div class="h-big" style="margin-top:6px;font-size:26px">Где вы находитесь</div><div class="sub">Гости увидят адрес и точку на карте.</div>
${FIELD('Город','Минск',{icon:'pin'})}
${FIELD('Адрес','ул. Немига, 13',{st:'focus',right:'<span class="link" style="font-size:13px">На карте</span>'})}
<div style="height:220px;border-radius:18px;margin-top:12px;background:url(map/minsk_dark.png) -380px -470px/1024px 1536px;position:relative"><div style="position:absolute;left:50%;top:50%;transform:translate(-50%,-100%);color:var(--pink)">${icf('pin','#FF1D8E').replace('class="ico"','class="ico" style="width:34px;height:34px;fill:#FF1D8E;stroke:#0B0B0F;stroke-width:1.4"')}</div><span class="tag glass" style="position:absolute;left:10px;bottom:10px">Сдвинь пин, если нужно</span></div>
<div class="t-cap" style="margin-top:12px;padding:0 4px;line-height:1.4">Район определится автоматически — по нему работает фильтр «Район» в афише.</div>
</div><div class="cta"><button class="btn pri" data-go="d6">Сохранить</button></div>${HI}`));

add('bnotif','biz','23 · Уведомления заведения','Отдельные от личных: оперативные приходят пушем, статистика — письмом раз в неделю. Личные уведомления аккаунта настраиваются в профиле.',
SCR('bnotif',`${NAV('Уведомления',{back:'d6'})}<div class="body">
<div class="grp-t" style="margin-top:4px">Оперативные</div><div class="grp">${LI('chat','Новые сообщения','мгновенно',{tg:true})}${LI('users','Заявки в команду','',{tg:true})}${LI('shield','Модерация событий','результат проверки',{tg:true})}</div>
<div class="grp-t">Раз в неделю</div><div class="grp">${LI('chart','Статистика на почту','понедельник утром',{tg:true})}${LI('bell','Дайджест событий','',{tg:false})}</div>
<div class="t-cap" style="margin-top:4px;padding:0 4px;line-height:1.4">Личные уведомления аккаунта настраиваются отдельно — в настройках профиля.</div>
</div>${HI}`));

/* ===== СИСТЕМНЫЕ СОСТОЯНИЯ ===== */
add('stEmpty','states','Пустое состояние · Сохранённое','Пустой экран всегда объясняет, что здесь будет, и даёт одно действие.',
SCR('stEmpty',`${NAV('Сохранённое',{back:'prof'})}<div style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;padding:0 44px;text-align:center"><div style="width:84px;height:84px;border-radius:28px;background:var(--card);display:flex;align-items:center;justify-content:center;color:var(--pink)">${ic('bookmark','style="width:36px;height:36px"')}</div><div class="t-title" style="margin-top:22px;font-size:24px">Здесь будут события, которые зацепили</div><div class="sub">Лайкни в свайпах или нажми на закладку в карточке.</div><button class="btn pri sm" style="margin-top:22px;height:44px;border-radius:22px;padding:0 22px">Открыть свайпы</button></div>${UTAB(4)}`),{state:true});
add('stLoad','states','Загрузка · скелетон','Скелетоны повторяют реальную раскладку экрана, без спиннеров на весь экран. Данные из кэша показываются сразу, свежие подгружаются поверх.',
SCR('stLoad',`<div style="position:absolute;top:58px;left:16px;right:16px;display:flex;justify-content:space-between"><img src="ph/logo.png" style="height:42px"><div style="display:flex;gap:8px"><div class="skel" style="width:90px;height:36px;border-radius:18px"></div><div class="skel" style="width:40px;height:40px;border-radius:20px"></div></div></div>
<div style="position:absolute;top:120px;left:16px;right:16px"><div class="skel" style="width:140px;height:14px;border-radius:7px"></div><div class="skel" style="width:240px;height:34px;border-radius:10px;margin-top:10px"></div><div style="display:flex;gap:8px;margin-top:18px">${[90,100,90,80].map(w=>`<div class="skel" style="width:${w}px;height:34px;border-radius:17px"></div>`).join('')}</div><div class="skel" style="height:262px;border-radius:28px;margin-top:16px"></div><div class="skel" style="width:120px;height:20px;border-radius:8px;margin-top:26px"></div><div style="display:flex;gap:12px;margin-top:12px">${[1,2,3].map(()=>`<div class="skel" style="width:150px;height:180px;border-radius:22px;flex:none"></div>`).join('')}</div></div>${UTAB(0)}`),{state:true});
add('stOff','states','Ошибка сети','Нет соединения: показываем кэш с плашкой и кнопкой «Повторить». Действия (лайк, «Иду») ставятся в очередь и отправляются при восстановлении.',
SCR('stOff',`<img src="out/raw/screen_02.png" style="position:absolute;inset:0;width:100%;height:100%;opacity:.35"><div class="toast" style="top:110px">${ic('wifiOff','style="color:var(--pink)"')}<div style="flex:1"><div style="font-size:14.5px;font-weight:650">Нет соединения</div><div class="t-cap" style="font-size:12.5px">Показываем сохранённое. Лайки отправим позже</div></div><button class="btn sec sm" style="height:32px;padding:0 12px;font-size:13px">Повторить</button></div>${UTAB(1)}`),{state:true});
add('stOk','states','Подтверждение · тост','Успешные действия подтверждаются тостом сверху на 2,5 с с возможностью отменить. Модальные «Готово!» не используем.',
SCR('stOk',`<img src="out/raw/screen_04.png" style="position:absolute;inset:0;width:100%;height:100%"><div class="toast"><div style="width:32px;height:32px;border-radius:50%;background:var(--lime);color:#0B0B0F;display:flex;align-items:center;justify-content:center">${ic('check','style="width:18px;height:18px;stroke-width:2.6"')}</div><div style="flex:1"><div style="font-size:14.5px;font-weight:650">Добавили в планы</div><div class="t-cap" style="font-size:12.5px">Сб, 24 окт · напомним за 3 часа</div></div><span class="link" style="font-size:14px">Отменить</span></div>`),{state:true});
