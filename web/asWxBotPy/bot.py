#asWxBotPy - Python Discord bot build P.6

import os
import discord
from dotenv import load_dotenv

load_dotenv()
TOKEN = os.getenv('DISCORD_TOKEN')
client = discord.Client()

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
		response = "Build 6 of asWxBotPy. Some snarky stuff being built in!"
		await message.channel.send(response)
	if ("dome") in (message.content).lower():
		response = "https://cdn.discordapp.com/attachments/623111643924135938/782953385551724544/image0-4.png"
		await message.channel.send(response)
	if ("moist") in (message.content).lower():
		response = "https://giphy.com/gifs/cbc-fridge-wars-fridgewars-fw105-L3L4zXkjFFwoQEcQMf"
		await message.channel.send(response)
	if ("pain" or "sciatic") in (message.content).lower():
		response = "Man, I feel your pain!"
		await message.channel.send(response)
	if ("what she said" or "twss") in (message.content).lower():
		response = "https://giphy.com/gifs/giphyqa-xMGh0bajSyNdC"
		await message.channel.send(response)
client.run(TOKEN)
