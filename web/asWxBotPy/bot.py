#asWxBotPy - Python Discord bot build P.4

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
		response = "Build 4 of asWxBotPy. Testing phase. Some snarky stuff being built in!"
		await message.channel.send(response)
	if "sciatic" in (message.content).lower():
		response = "Man, I feel your pain!"
		await message.channel.send(response)
client.run(TOKEN)
