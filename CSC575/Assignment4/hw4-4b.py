import sys
import math

data=[[0, 0, 1, 1, 1], [0, 0, 0, 1, 1], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0], [0, 0, 0, 0, 0]]

def hits(data, count, auth, hub):
	M = len(auth)
	new_auth = [0.0 for x in range(M)]
	new_hub = [0.0 for x in range(M)]

	for i in range(M):
		for j in range(M):
			if data[j][i] == 1:
				new_auth[i] += hub[j]
			if data[i][j] == 1:
				new_hub[i] += auth[j]
	print 'iteration {0}:'.format(count + 1)
	print 'before norm:'
	print '\tauth: ', ' '.join(str(format(x, '.2f')) for x in new_auth)
	print '\thub:  ', ' '.join(str(format(x, '.2f')) for x in new_hub)
	
	c1 = math.sqrt(sum([x**2 for x in new_auth]))
	c2 = math.sqrt(sum([x**2 for x in new_hub]))
	norm_auth = [x / c1 for x in new_auth]
	norm_hub = [x / c2 for x in new_hub]
	print 'after norm: '
	print '\tauth: ', ' '.join(str(format(x, '.2f')) for x in norm_auth)
	print '\thub:  ', ' '.join(str(format(x, '.2f')) for x in norm_hub)
	print ''

	count += 1
	if count >= 3:
		return
	else:
		hits(data, count, norm_auth, norm_hub)

print 'Executing hits algorithm:'

auth = [1.0 for x in range(len(data))]
hub = [1.0 for x in range(len(data))]
hits(data, 0, auth, hub)
