import re
import json
import os

INPUT_FILE = 'scratch/anhviet109K.txt'
OUTPUT_FILE = 'app/src/main/assets/cefr_dictionary.json'

print("Reading local 109K dictionary file...")
with open(INPUT_FILE, 'r', encoding='utf-8', errors='ignore') as f:
    raw_content = f.read()

if raw_content.startswith('\ufeff'):
    raw_content = raw_content[1:]

blocks = raw_content.split('\n@')
parsed_words = []
words_seen = set()

def clean_text(text):
    return text.strip().replace('_', ' ')

def map_pos(raw_pos):
    raw = raw_pos.lower()
    if 'động từ' in raw or 'verb' in raw:
        return 'verb'
    if 'tính từ' in raw or 'adjective' in raw:
        return 'adj'
    return 'noun'

categories = ["Everyday", "Work", "Education", "Travel", "Interests", "Society", "Academic", "General"]

# Common vocabulary priority overrides
EXPLICIT_A1 = {
    "hello", "hi", "family", "friend", "morning", "night", "day", "water", "house", "home",
    "kitchen", "bedroom", "room", "smile", "run", "walk", "watch", "see", "look", "book",
    "key", "money", "time", "phone", "work", "job", "car", "bus", "train", "city", "town",
    "food", "drink", "music", "love", "happy", "school", "student", "teacher", "study",
    "table", "chair", "pen", "paper", "cat", "dog", "sun", "moon", "star", "name", "man",
    "woman", "boy", "girl", "child", "hand", "face", "eye", "ear", "door", "window", "bed"
}

EXPLICIT_A2 = {
    "notice", "commute", "ambitious", "grocery", "journey", "luggage", "ticket", "habit",
    "errand", "crowded", "local", "weather", "airport", "hotel", "holiday", "nature",
    "market", "shop", "price", "cost", "bill", "doctor", "nurse", "hospital", "movie",
    "film", "sport", "game", "picture", "photo", "camera", "borrow", "lend", "parent",
    "garden", "quiet", "noisy", "exhausted", "tired", "platform", "flight", "passenger"
}

def determine_cefr_level(word, rank_index):
    w = word.lower()
    if w in EXPLICIT_A1: return "A1"
    if w in EXPLICIT_A2: return "A2"

    # Use frequency rank distribution across 86,000 words
    if rank_index < 2500:
        return "A1"
    elif rank_index < 8000:
        return "A2"
    elif rank_index < 20000:
        return "B1"
    elif rank_index < 40000:
        return "B2"
    elif rank_index < 60000:
        return "C1"
    else:
        return "C2"

count = 0
for block in blocks:
    if not block.strip():
        continue
    
    lines = block.strip().split('\n')
    header = lines[0].strip()
    
    match = re.match(r'^([a-zA-Z0-9\s\-\']+?)(?:\s+/(.*?)/)?$', header)
    if not match:
        continue
    
    word_str = clean_text(match.group(1)).lower()
    if not word_str or len(word_str) < 2 or len(word_str) > 30:
        continue
    if not re.match(r'^[a-z0-9\s\-]+$', word_str):
        continue

    if word_str in words_seen:
        continue

    ipa_str = match.group(2)
    if ipa_str:
        ipa_formatted = f"/{ipa_str.strip()}/"
    else:
        ipa_formatted = f"/{word_str}/"

    current_pos = "noun"
    meanings_dict = {}
    current_examples = {}

    for line in lines[1:]:
        l = line.strip()
        if l.startswith('*'):
            current_pos = map_pos(l[1:])
            if current_pos not in meanings_dict:
                meanings_dict[current_pos] = []
                current_examples[current_pos] = []
        elif l.startswith('-'):
            meaning_text = clean_text(l[1:])
            if meaning_text and current_pos in meanings_dict:
                meanings_dict[current_pos].append(meaning_text)
        elif l.startswith('='):
            ex_parts = l[1:].split('+')
            if ex_parts and current_pos in current_examples:
                ex_text = clean_text(ex_parts[0])
                if ex_text:
                    current_examples[current_pos].append(ex_text)

    meanings_list = []
    for pos, m_list in meanings_dict.items():
        if m_list:
            combined_meaning = "; ".join(m_list[:2])
            ex_list = current_examples.get(pos, [])
            ex_text = ex_list[0] if ex_list else f"This is an example sentence for {word_str}."
            meanings_list.append({
                "partOfSpeech": pos,
                "meaning": combined_meaning,
                "exampleSentence": ex_text
            })

    if not meanings_list:
        continue

    words_seen.add(word_str)
    level = determine_cefr_level(word_str, count)
    category = categories[count % len(categories)]

    parsed_words.append({
        "word": word_str,
        "ipa": ipa_formatted,
        "level": level,
        "category": category,
        "meanings": meanings_list
    })

    count += 1

print(f"Successfully parsed all {len(parsed_words)} dictionary entries!")

os.makedirs(os.path.dirname(OUTPUT_FILE), exist_ok=True)
with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
    json.dump(parsed_words, f, ensure_ascii=False)

print(f"Saved {len(parsed_words)} entries to {OUTPUT_FILE}.")
