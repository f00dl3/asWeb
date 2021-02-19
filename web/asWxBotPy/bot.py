#asWxBotPy - Python Discord bot build P.11

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
		response = "Build 11 of asWxBotPy. Some snarky stuff being built in!"
		await message.channel.send(response)
	if ("c2f") in (message.content).lower():
		response = apiCallC2fF2c("c2f", message.content)
		await message.channel.send(response)
	if ("dome") in (message.content).lower():
		response = "https://cdn.discordapp.com/attachments/623111643924135938/782953385551724544/image0-4.png"
		await message.channel.send(response)
	if ("f2c") in (message.content).lower():
		response = apiCallC2fF2c("f2c", message.content)
		await message.channel.send(response)
	if ((("moist ") in (message.content).lower()) or (message.content.lower() == "moist")):
		response = "https://giphy.com/gifs/cbc-fridge-wars-fridgewars-fw105-L3L4zXkjFFwoQEcQMf"
		await message.channel.send(response)
	if ("sciatic") in (message.content).lower():
		response = "Man, I feel your pain!"
		await message.channel.send(response)
	if ("what she said" or "twss") in (message.content).lower():
		response = "https://giphy.com/gifs/giphyqa-xMGh0bajSyNdC"
		await message.channel.send(response)
client.run(TOKEN)
