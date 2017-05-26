var express = require('express');
var app = express();
var mysql = require('mysql');
var fs = require('fs');
var gm = require('gm');
var async = require('async');
var navigator = require('navigator');
require('date-utils');
var d = new Date();

var pool = mysql.createPool({
	connectionLimit: 10,
	user: 'root',
	password: '',
	database: 'zorzima'
	});

pool.getConnection(function(err){
	if(err)
	{
		console.error('mysql error');
		console.error(err);
		throw(err);
	}
	else
	{
		console.log('database connection succeed');
	}
});

app.get('/', function(req,res){
	checkUser();
	res.send('Final example page');
});

app.get('/:select', function(req,res){
	if(req.params.select == 'login')
		login(req,res);

	else if(req.params.select == 'btn')
		btn(req,res);

	else if(req.params.select == 'zorgi')
		zorgi(req,res);

	else if(req.params.select == 'imgs')
		imgs(req,res);

	else if(req.params.select == 'logout')
		logout(req,res);

	else if(req.params.select == 'winlist')
		winlist(req,res);

	else if(req.params.select == 'chance')
		chance(req,res);
});

app.listen(3000);		// 80 수정

/*function login(req,res)		// 로그인 함수
{
	var nick_name = req.param('n');
	var email = req.param('e');

	pool.query('SELECT email from member where email=?', email, function(err,result) {
		var string = JSON.stringify(result);

		if(string=='[]')
		{
			pool.query('INSERT INTO member values(?,?,?,?)', [nick_name,email,2,null], function(err) {
				if(err) {throw err;}
				else
					console.log(email + ' login');
			});
		}

		else
			console.log('existing user');
	});
	res.end('login complete');
}*/

//login patch
function login(req,res)		// 로그인 함수
{
	var nick_name = req.param('n');
	var email = req.param('e');

	pool.query('SELECT email from member where email=?', email, function(err,result) {
		var string = JSON.stringify(result);

		if(string=='[]')		// sign in
		{
			pool.query('INSERT INTO member values(?,?,?,?,?,?,?)', [nick_name,email,2,null,date(),0,1], function(err) {
				if(err) {throw err;}
				else
					console.log(email + ' sign in');
					res.end('1 DAY STREAK!')
			});
		}

		else					// existing user
		{
			pool.query('SELECT first from member where email=?', email, function(err, r) {
				var f = JSON.stringify(r);
				f = f.split(':');
				f = f[1].split('}');
				f = Number(f[0]);

				if(f==1)		// first login
				{
					pool.query('SELECT last_access from member where email=?', email, function(err, r) {
						var a = JSON.stringify(r);
						a = a.split(':');
						a = a[1].split('}');
						a = Number(a[0]);

						if(date()-a == 1 || date()-a < 0)		// ~ streak
						{
							pool.query('SELECT streak from member where email=?', email, function(err,r) {
								var s = JSON.stringify(r);
								s = s.split(':');
								s = s[1].split('}');
								s = Number(s[0]);
								s = s+1;

								if(s==7)		// if streak is 7, give reward
								{
									pool.query('SELECT chance_z from member where email=?', email, function(err,r) {
										var c = JSON.stringify(r);
										c = c.split(':');
										c = c[1].split('}');
										c = Number(c[0])+1;

										pool.query('UPDATE member set last_access='+date()+', first=0, streak=0, chance_z='+c+' where email=?', email, function(err) {
											res.end('7 DAY STREAK!!! ONE MORE CHANCE!');
										});
									});
								}

								else
								{
									pool.query('UPDATE member set last_access='+date()+', first=0, streak='+s+' where email=?', email, function(err) {
										res.end(s + ' DAY STREAK!');
									});
								}
							});
						}

						else					// 1day
						{
							pool.query('UPDATE member set last_access='+date()+', first=0, streak=1 where email=?', email);
							res.end('1 DAY STREAK!');
						}
					});
				}

				else			// not first
				{
					res.end('login success');
				}
			});
		}
	});
}
//login patch

function logout(req,res)
{
	var email = req.param('e');
	var num;

	pool.query('DELETE from member where email=?', email, function() {
		pool.query('SELECT count(*) from product', function(err,r) {
			if(err) {throw err;}
			else{
				num = JSON.stringify(r);
				num = num.split(':');
				num = num[1].split('}');
				num = Number(num[0]); 

			repeat(num); }
		});
	});

	console.log('logout');
	res.end('logout');
}

function btn(req,res)		// 조르기 버튼 함수
{
	var p_number = req.param('p_number');
	var email = req.param('e');

	pool.query('SELECT chance_z,time from member where email=?', email, function(err,result)	{
		if(err) {throw err;}
		else
		{
			var string=JSON.stringify(result);
			var str = string.split(':');
			var f = str[1].split(',');
			var t = str[2].split('}');
			var t = Number(t[0].replace(/\"/gi,''));
			
			if(Number(f[0])==0)
				res.end('zorgi chance empty');
			else if(Time()-t>=0 && Time()-t<5)
				res.end('try it later');
			else
			{
				var temp = Number(f[0]) - 1;
				pool.query('UPDATE member SET chance_z=' +temp+' where email=?', email)
				add(p_number, email, res);
			}
		}
	});
}

function zorgi(req,res)		// 조르기 현황
{
	var p_number = req.param('p_number');

	pool.query('SELECT num_zorzima_p, upto_num_people from product where number=?', p_number, function(err, r) {
		var string = JSON.stringify(r);
		var str = string.split(':');
		var temp = str[1].split(',');

		res.end(temp[0]);
	});
}

function imgs(req,res)		// 이미지
{
	var path;
	var p_number = req.param('p_number');

	pool.query('SELECT image from product where number=?', p_number, function(err, result) {
		var str = JSON.stringify(result);
		str = str.split(':');
		str = str[1].split('}');
		str = str[0].replace(/\"/gi, '');
		path = str;
		fs.readFile(path, function(err,data){
			res.writeHead(200, {'Content-Type': 'image/png'});
			res.end(data);
			});
	});
}

function winlist(req,res)		// 최근 3개 출력 구현 예정
{
	pool.query('SELECT email,w_product from winner order by recent desc limit 3', function(err, result) {
		if(err) {throw err;}
		else
		{
			var list = JSON.stringify(result);

			res.end(list);
		}
	});
}

function chance(req,res)
{
	var email = req.param('e');

	pool.query('SELECT chance_z from member where email=?', email, function(err, r) {
		var str = JSON.stringify(r);
		str = str.split(':');
		str = str[1].split('}');
		str = str[0];

		res.end(str);
	});
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function add(p_number, email, res)
{
	pool.query('SELECT z_product from zorzima where z_product=?', p_number, function(err, result) {
		if(err) 
			throw err;
		else
		{
			var string = JSON.stringify(result);

			if(string!='[]')
			{	
				pool.query('SELECT num_zorzima_p,upto_num_people from product where number=?', p_number, function(err, r) {
					if(err)
						throw err;
					else
					{
						var string = JSON.stringify(r);
						var str = string.split(':');
						var temp = str[1].split(',');
						var m = str[2].split('}');		// upto_num_people
						var add_number = Number(temp[0])+1;		// num_zorzima_p

						if(Number(m[0])==add_number)
						{
							pool.query('INSERT INTO zorzima values(?,?)', [p_number, email]);
							pool.query('UPDATE product set num_zorzima_p=' + add_number + ' where number=?', p_number);
							pool.query('UPDATE member set time='+Time()+' where email=?', email);

							pool.query('SELECT email from zorzima where z_product=?',p_number, function(err, list) {
								list = JSON.stringify(list);
								list = list.replace(/:/gi,'');
								list = list.replace(/email/gi,'');
								list = list.replace(/{/gi,'');
								list = list.replace(/}/gi,'');
								list = list.replace(/\"/gi,'');
								list = list.replace(/\[/gi,'');
								list = list.replace(/\]/gi,'');
								list = list.split(',');
								var winner = randomRange(0,Number(m[0])-1);	// 당첨자 추첨
								console.log(p_number+' : '+list[winner] + ' from : '+winner);

								// before patch
								//pool.query('INSERT INTO winner values(?,?)', [list[winner], p_number]);

								// winlist patch
								pool.query('SELECT recent from winner order by recent desc limit 1', function(err,r) {
									var str = JSON.stringify(r);
									var num;

									if(str=='[]')
										num = 0;
									else
									{
										str = str.split(':');
										str = str[1].split('}');
										num = Number(str[0]);
									}

									pool.query('INSERT INTO winner values(?,?,?)',[list[winner],p_number,num+1]);
								});
								// winlist patch
							});
							res.end('lottery');
						}
						
						else if(m[0] > add_number)
						{
							pool.query('INSERT INTO zorzima values(?,?)', [p_number, email]);
							pool.query('UPDATE product set num_zorzima_p=' + add_number + ' where number=?', p_number);
							pool.query('UPDATE member set time='+Time()+' where email=?', email);
							res.end('zorgi complete');
						}

						else
							res.send('already finished');
					}
				});
			}

			else
			{
				pool.query('INSERT INTO zorzima values(?,?)', [p_number, email]);
				pool.query('UPDATE product set num_zorzima_p=1 where number=?', p_number);
				pool.query('UPDATE member set time='+Time()+' where email=?', email);
				res.end('first zorgi');
			}
		}
	});
}

function repeat(i)
{
	var rp = i;

	pool.query('SELECT count(*) from zorzima where z_product=?', rp, function(err,r) {
		var num = JSON.stringify(r);
		num = num.split(':');
		num = num[1].split('}');
		num = Number(num[0]);

		pool.query('UPDATE product set num_zorzima_p='+num+' where number=?', rp, function(err) {
			if(rp-1!=0)
				repeat(rp-1);
		});
	});
}

function randomRange(n1, n2)		// 난수 생성
{
  return Math.floor( (Math.random() * (n2 - n1 + 1)) + n1 );
}

function Time()
{
	var h = d.toFormat('HH24');
	var m = d.toFormat('MI');
	var cal = Number(h)*60 + Number(m);

	return cal;
}

function date()
{
	var year = d.toFormat('YYYY');
	var month = Number(d.toFormat('M'));
	var day = Number(d.toFormat('D'));
	var D = 0;

	for(var i=1; i<month; i++)
	{
		if(i==1 || i==3 || i==5 || i==7 || i==8 || i==10 || i==12)
			D = D+31;
		else if(i==2)
		{
			if((year%4==0 && year%100!=0) || year%400==0)
				D = D+29;
			else
				D = D+28;
		}

		else
			D = D+30;	
	}

	D = D+day;

	return D;
}

function checkUser()
{
	var Mobile = false;
	var AgentStr = navigator.userAgent//.toLowerCase();

	console.log(AgentStr);
}

function update()		// 00시에 db 갱신
{
	d.setTimeToNow();
	if(d.toFormat('HH24-MI')=='00-00')
	{
		pool.query('UPDATE member SET chance_z = 2');		// 조르기 횟수 조정
		pool.query('UPDATE member SET first = 1');		// login patch
	}
}

setInterval(update, 59000);
