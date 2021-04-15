#asWxBotPy - Python Discord bot build P.14

import os
import discord
import logging
import requests
from dotenv import load_dotenv

load_dotenv()
TOKEN = os.getenv('DISCORD_TOKEN')
client = discord.Client()
apiBase = "https://localhost:8444/asWeb/r/"

def apiCallC2fF2c(itype, dataIn):
	if(itype == "f2c"): 
		inType = "F"
		retType = "C"
	else:
		inType = "C"
		retType = "F"
	print(str(inType))
	valueIn = dataIn.split()
	url = apiBase + "Wx"
	response = requests.post(
		url, 
		data = {
			'doWhat': 'getC2fF2c',
			'convType': itype,
			'temperature': valueIn[1]
		},
		verify=False
	)
	logging.warning("Response ", response.text)	
	dataBack = valueIn[1] + inType + " is " + str(round(float(response.text),2)) + retType
	return str(dataBack);

@client.event
async def on_ready():
	print(f'{client.user} has connected to Discord!')

@client.event
async def on_member_join(member):
	await member.create_dm();
	await member.dm_channel.send(f'Hi {member.name}, welcome to the kcregionalwx Discord')

@client.event
async def on_message(message):
	if message.author == client.user:
		return
	if message.content == '!pHelp':
		response = "Build 14 of asWxBotPy. Some snarky stuff being built in!"
		await message.channel.send(response)
	if ("brb") in (message.content).lower():
		response = "https://media4.giphy.com/media/zZeCRfPyXi9UI/giphy.gif"
		await message.channel.send(response)
	if ("c2f") in (message.content).lower():
		response = apiCallC2fF2c("c2f", message.content)
		await message.channel.send(response)
	if ("dome") in (message.content).lower():
		response = "https://cdn.discordapp.com/attachments/623111643924135938/782953385551724544/image0-4.png"
		await message.channel.send(response)
	if ("don't know") in (message.content).lower():
		response = "https://tenor.com/view/obi-wan-confusion-gif-10824788"
		await message.channel.send(response)
	if ("f2c") in (message.content).lower():
		response = apiCallC2fF2c("f2c", message.content)
		await message.channel.send(response)
	if ("meh" or "blah" or "pfft") in (message.content).lower():
		response = "https://giphy.com/gifs/bfd-3o6UB2MSoh7z6Gw3fO"
		await message.channel.send(response)
	if ((("moist ") in (message.content).lower()) or (message.content.lower() == "moist")):
		response = "https://giphy.com/gifs/cbc-fridge-wars-fridgewars-fw105-L3L4zXkjFFwoQEcQMf"
		await message.channel.send(response)
	if ("pouring") in (message.content).lower():
		response = "https://giphy.com/gifs/evan-rachel-wood-thirteen-TVpeXDi8xTlyo"
		await message.channel.send(response)
	if ("sciatic") in (message.content).lower():
		response = "Man, I feel your pain!"
		await message.channel.send(response)
	if ("stock") in (message.content).lower():
		response = "https://giphy.com/gifs/nyse-stock-market-stocks-AgHBbekqDik0g"
		await message.channel.send(response)
	if ("stonk") in (message.content).lower():
		response = "https://tenor.com/view/stonks-meme-gif-16107418"
		await message.channel.send(response)
	if ("what she said" or "twss") in (message.content).lower():
		response = "https://giphy.com/gifs/giphyqa-xMGh0bajSyNdC"
		await message.channel.send(response)
client.run(TOKEN)
